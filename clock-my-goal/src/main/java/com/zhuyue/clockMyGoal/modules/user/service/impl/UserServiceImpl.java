package com.zhuyue.clockMyGoal.modules.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Resource;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

import com.zhuyue.clockMyGoal.common.authentication.BlackListService;
import com.zhuyue.clockMyGoal.common.authentication.JwtService;
import com.zhuyue.clockMyGoal.common.constants.*;
import com.zhuyue.clockMyGoal.common.exception.*;
import com.zhuyue.clockMyGoal.common.storeage.*;
import com.zhuyue.clockMyGoal.common.util.TimeService;
import com.zhuyue.clockMyGoal.modules.user.dto.request.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserLoginReponse;
import com.zhuyue.clockMyGoal.modules.user.entity.*;
import com.zhuyue.clockMyGoal.modules.user.mapper.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import java.util.stream.Collectors;
import java.util.List;

import java.io.IOException;

/**
 * 用户服务实现类
 * UserServiceImpl
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
  // 注入Mapper代理对象
  @Resource()
  private UserMapper userMapper;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private BlackListService blackListService;
  @Autowired
  private StoreageService storeageService;

  private UserResponse convertToResponse(User user) {
    // 使用Builder模式将用户信息转换为响应对象
    String createTime = TimeService.getTimeStringByTime(user.getCreateTime());
    String updateTime = TimeService.getTimeStringByTime(user.getUpdateTime());

    return UserResponse.builder()
        .id(user.getId())
        .userName(user.getUserName())
        .age(user.getAge())
        .sex(user.getSex())
        .avatarUrl(user.getAvatarUrl())
        .createTime(createTime)
        .updateTime(updateTime)
        .status(user.getStatus())
        .build();
  }

    // 根据用户名查询用户
    User user = userMapper.selectByUserName(dto.getUserName());
    // 如果用户不存在，则抛出异常
    if (user == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound);
    }

    // 如果密码不匹配，则抛出异常
    if (!user.getPassword().equals(dto.getPassword())) {
      throw new BusinessException(ExceptionCodeEnum.UserNameOrPasswordError);
    }

    // 如果用户状态为禁用，则抛出异常
    if (user.getStatus() == 0) {
      throw new BusinessException(ExceptionCodeEnum.UserStatusDisabled);
    }

    // 更新redis中的用户状态
    updateUserStatusInRedis(user.getId(), user.getStatus());

    // 生成令牌
    String token = jwtService.generateToken(user.getId(), user.getUserName());
    UserLoginReponse userLoginReponse = UserLoginReponse.builder()
      .user(convertToResponse(user))
      .token(token)
      .build();

    return userLoginReponse;
  }

  @Override
  public UserCredentialDto getUserCredentialByUserName(String userName) {
    User user = userMapper.selectByUserName(userName);
    if (user == null) {
      return null;
    }
    return UserCredentialDto.builder().id(user.getId()).userName(user.getUserName()).password(user.getPassword()).status(user.getStatus()).build();
  }

  @Override
  public String uploadAvatar(long userId, MultipartFile file) {
    String avatarUrl = null;
    try {
      avatarUrl = storeageService.uploadImage(file);
      userMapper.updateAvatarByUserId(userId, avatarUrl);
    } catch (BusinessException e) {
      throw new BusinessException(e.getCode(), e.getMessage());
    } catch (IOException e) {
      throw new BusinessException(ExceptionCodeEnum.FileUploadError.getCode(), e.getMessage());
    }
    return avatarUrl;
  }

  @Override
  public UserResponse getUserByUserId(Long userId) {
    User user = userMapper.selectByUserId(userId);
    if (user == null) {
      return null;
    }
    return convertToResponse(user);
  }

  @Override 
  public PageResponse<UserResponse> getUserList(UserListRequest userListRequest) {
    int pageSize = userListRequest.getPageSize();
    int pageNum = userListRequest.getPageNum();
    String keyword = userListRequest.getKeyword();
    Integer status = userListRequest.getStatus();
    int offset = (pageNum - 1) * pageSize;
    List<User> users = userMapper.selectUserList(keyword, status, offset, pageSize);
    long total = getUserListTotal(keyword, status);
    
    PageResponse<UserResponse> userListResponse = PageResponse.<UserResponse>builder()
      .pageSize(pageSize)
      .pageNum(pageNum)
      .data(users.stream().map(this::convertToResponse).collect(Collectors.toList()))
      .total(total)
      .build();

    return userListResponse;
  }

  @Override
  public long getUserListTotal(String keyword, Integer status) {
    long total = userMapper.selectCountUsers(keyword, status);
    return total;
  }

  @Override
  public void updateUserStatus(Long userId, Integer status) {
    userMapper.updateUserStatus(userId, status);
    updateUserStatusInRedis(userId, status);
  }

  @Override
  public void updateUserStatusInRedis(Long userId, Integer status) {
    blackListService.addUserStatusToRecord(userId, status);
  }
}