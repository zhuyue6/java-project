package com.zhuyue.clockMyGoal.modules.admin.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuyue.clockMyGoal.common.authentication.BlackListService;
import com.zhuyue.clockMyGoal.common.authentication.JwtService;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.common.util.TimeService;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserLoginResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.UserInfoResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.UserPlanResponse;
import com.zhuyue.clockMyGoal.modules.admin.entity.AdminUser;
import com.zhuyue.clockMyGoal.modules.admin.mapper.AdminUserMapper;
import com.zhuyue.clockMyGoal.modules.admin.service.AdminService;
import com.zhuyue.clockMyGoal.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockMyGoal.modules.goal.service.GoalService;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockMyGoal.modules.label.service.LabelService;
import com.zhuyue.clockMyGoal.modules.user.entity.User;
import com.zhuyue.clockMyGoal.modules.user.mapper.UserMapper;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserListRequest;

@Service("AdminService")
public class AdminServiceImpl implements AdminService {
  @Autowired
  private AdminUserMapper adminUserMapper;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private BlackListService blackListService;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserService userService;
  @Autowired
  private GoalService goalService;
  @Autowired
  private LabelService labelService;

  @Override
  /**  管理平台的用户登录
   * @param dto
   * @return AdminUserLoginResponse
   * @see com.zhuyue.clockMyGoal.modules.admin.service.AdminService#login(com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest)
   */
  public AdminUserLoginResponse login(AdminUserLoginRequest dto) {
    AdminUser adminUser = adminUserMapper.selectAdminUserByUserName(dto.getUserName());

    if (adminUser == null) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserNotFound);
    }

    if (!adminUser.getPassword().equals(dto.getPassword())) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserNameOrPasswordError);
    }

    if (Objects.equals(adminUser.getStatus(), 0)) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserStatusDisabled);
    }

    String token = jwtService.generateToken(adminUser.getId(), adminUser.getUserName(), "admin");


    AdminUserLoginResponse adminUserLoginResponse = new AdminUserLoginResponse();
    adminUserLoginResponse.setId(adminUser.getId());
    adminUserLoginResponse.setUserName(adminUser.getUserName());
    adminUserLoginResponse.setCreateTime(formatTime(adminUser.getCreateTime()));
    adminUserLoginResponse.setUpdateTime(formatTime(adminUser.getUpdateTime()));
    adminUserLoginResponse.setToken(token);
    adminUserLoginResponse.setStatus(adminUser.getStatus());
    adminUserLoginResponse.setRole(adminUser.getRole() == null ? null : String.valueOf(adminUser.getRole()));
    return adminUserLoginResponse;
  }

  @Override
  public AdminUserResponse getAdminUserInfo(Long id) {
    AdminUser adminUser = adminUserMapper.selectAdminUserById(id);
    if (adminUser == null) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserNotFound);
    }


    AdminUserResponse adminUserResponse = new AdminUserResponse();
    adminUserResponse.setId(adminUser.getId());
    adminUserResponse.setUserName(adminUser.getUserName());
    adminUserResponse.setCreateTime(formatTime(adminUser.getCreateTime()));
    adminUserResponse.setUpdateTime(formatTime(adminUser.getUpdateTime()));
    adminUserResponse.setStatus(adminUser.getStatus());
    adminUserResponse.setRole(adminUser.getRole() == null ? null : String.valueOf(adminUser.getRole()));
    return adminUserResponse;
  }

  @Override
  public void logout(String authHeader) {
    String token = jwtService.getToken(authHeader);
    long remainSeconds = jwtService.getTokenRemainSeconds(token);
    if (remainSeconds > 0) {
      blackListService.addTokenToBlacklist(token, remainSeconds);
    }
  }

  @Override
  public PageResponse<UserResponse> getUserList(UserListRequest request) {
    PageResponse<UserResponse> userListResponse = userService.getUserList(request);
    return userListResponse;
  }

  @Override
  public UserInfoResponse getUserInfo(long id) {
    // 获取用户详情
    UserResponse user = userService.getUserByUserId(id);
    if (user == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound);
    }
    
    // 获取用户所有的目标
    List<GoalResponse> goals = goalService.getGoals(user.getId());
    if (goals == null) {
      goals = List.of();
    }
    int ongoing = 0;
    int completed = 0;
    int expired = 0;
    for (GoalResponse goal : goals) {
      if (goal.getStatus() == 1) {
        ongoing += 1;
      } else if (goal.getStatus() == 2) {
        completed += 1;
      } else if (goal.getStatus() == 3) {
        expired += 1;
      }
    }

    List<GoalResponse> latest = goals.stream()
        .sorted(Comparator.comparing(GoalResponse::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())))
        .limit(10)
        .toList();
    List<UserPlanResponse> plans = new ArrayList<>();
    for (GoalResponse goal : latest) {
      plans.add(UserPlanResponse.builder()
          .id(goal.getId())
          .name(goal.getRemark() == null ? "" : goal.getRemark())
          .cycle(goal.getClockInCycle())
          .clockInCount(goal.getClockInCount())
          .targetClockInCount(goal.getTargetClockInCount())
          .status(goal.getStatus())
          .expireTime(goal.getExpirationTime() == null ? "" : goal.getExpirationTime())
          .createTime(goal.getCreateTime() == null ? "" : goal.getCreateTime())
          .build());
    }

    List<LabelResponse> labels = labelService.getLabelListByUserId(user.getId());
    int labelCount = labels == null ? 0 : labels.size();

    return UserInfoResponse.builder()
        .id(user.getId())
        .userName(user.getUserName())
        .sex(user.getSex())
        .age(user.getAge())
        .avatarUrl(user.getAvatarUrl())
        .status(user.getStatus())
        .createTime(user.getCreateTime())
        .ongoing(ongoing)
        .completed(completed)
        .expired(expired)
        .labelCount(labelCount)
        .plans(plans)
        .build();
  }

  @Override
  public void updateUserStatus(Long id, Integer status) {
    if (status == null || (status != 0 && status != 1)) {
      throw new BusinessException(ExceptionCodeEnum.InvalidParameters);
    }
    requireUser(id);
    userService.updateUserStatus(id, status);
  }

  private User requireUser(Long id) {
    User user = userMapper.selectByUserId(id);
    if (user == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound);
    }
    return user;
  }

  private String formatTime(LocalDateTime time) {
    if (time == null) {
      return "";
    }
    return TimeService.getTimeStringByTime(time);
  }
}
