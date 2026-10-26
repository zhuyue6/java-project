package com.zhuyue.clockMyGoal.modules.admin.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuyue.clockMyGoal.modules.admin.service.AdminPlatformUserService;
import com.zhuyue.clockMyGoal.modules.admin.mapper.AdminUserMapper;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.PlatformUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.PlatformUserListRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.PlatformUserPlanResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockMyGoal.modules.goal.service.GoalService;
import com.zhuyue.clockMyGoal.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockMyGoal.modules.label.service.LabelService;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserListRequest;

@Service("AdminPlatformUserService")
public class AdminPlatformUserServiceImpl implements AdminPlatformUserService {
  @Autowired
  private UserService userService;

  @Autowired
  private GoalService goalService;

  @Autowired
  private LabelService labelService;

  private PlatformUserResponse convertToPlatformUserResponse(UserResponse user, List<GoalResponse> goals) {
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
    List<PlatformUserPlanResponse> plans = new ArrayList<>();
    for (GoalResponse goal : latest) {
      plans.add(PlatformUserPlanResponse.builder()
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

    return PlatformUserResponse.builder()
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
  public PlatformUserResponse getUserById(long id) {
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
    
    PlatformUserResponse platformUserResponse = convertToPlatformUserResponse(user, goals);

    return platformUserResponse;
  }

  @Override
  public PageResponse<UserResponse> getUserList(PlatformUserListRequest platformUserListRequest) {
    UserListRequest userListRequest = new UserListRequest();
    userListRequest.setKeyword(platformUserListRequest.getKeyword());
    userListRequest.setStatus(platformUserListRequest.getStatus());
    userListRequest.setPageNum(platformUserListRequest.getPageNum());
    userListRequest.setPageSize(platformUserListRequest.getPageSize());

    PageResponse<UserResponse> userListResponse = userService.getUserList(userListRequest);

    return userListResponse;
  }

  @Override
  public void updateUserStatus(long id, int status) {
    userService.updateUserStatus(id, status);
  }
}