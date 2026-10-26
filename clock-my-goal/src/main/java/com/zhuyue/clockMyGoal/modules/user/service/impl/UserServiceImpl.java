package com.zhuyue.clockMyGoal.modules.user.service.impl;

import java.util.stream.Collectors;
import java.util.List;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import com.zhuyue.clockMyGoal.common.authentication.BlackListService;
import com.zhuyue.clockMyGoal.common.constants.*;
import com.zhuyue.clockMyGoal.common.exception.*;
import com.zhuyue.clockMyGoal.common.storeage.*;
import com.zhuyue.clockMyGoal.common.util.TimeService;
import com.zhuyue.clockMyGoal.modules.user.dto.request.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.*;
import com.zhuyue.clockMyGoal.modules.user.entity.*;
import com.zhuyue.clockMyGoal.modules.user.mapper.*;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;

/**
 * 用户服务实现类
 * UserServiceImpl
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
  // 注入Mapper代理对象
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private BlackListService blackListService;
  @Autowired
  private StoreageService storeageService;

  private UserResponse convertToUserResponse(User user) {
    // 使用Builder模式将用户信息转换为响应对象
    String createTime = TimeService.getTimeStringByTime(user.getCreateTime());
    String updateTime = TimeService.getTimeStringByTime(user.getUpdateTime());
    
    UserResponse userResponse = new UserResponse();
    userResponse.setId(user.getId());
    userResponse.setUserName(user.getUserName());
    userResponse.setAge(user.getAge());
    userResponse.setSex(user.getSex());
    userResponse.setAvatarUrl(user.getAvatarUrl());
    userResponse.setCreateTime(createTime);
    userResponse.setUpdateTime(updateTime);
    userResponse.setStatus(user.getStatus());
    return userResponse;
  }

  @Override 
  public long createUser(@Valid CreateUserRequest createUserRequest) {
    User user = new User();
    user.setUserName(createUserRequest.getUserName());
    user.setPassword(createUserRequest.getPassword());
    user.setSex(createUserRequest.getSex());
    user.setAge(createUserRequest.getAge());
    user.setStatus(1);
    userMapper.insertUser(user);
    return user.getId();
  }

  @Override
  public UserCredentialRequest getUserCredentialByUserName(String userName) {
    User user = userMapper.selectByUserName(userName);
    if (user == null) {
      return null;
    }

    UserCredentialRequest userCredentialRequest = new UserCredentialRequest();
    userCredentialRequest.setId(user.getId());
    userCredentialRequest.setUserName(user.getUserName());
    userCredentialRequest.setPassword(user.getPassword());
    userCredentialRequest.setStatus(user.getStatus());
    return userCredentialRequest;
  }

  @Override
  public String uploadUserAvatar(long userId, MultipartFile file) {
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
    User user = requireUser(userId);
    return convertToUserResponse(user);
  }

  @Override 
  public PageResponse<UserResponse> getUserList(UserListRequest userListRequest) {
    int pageSize = userListRequest.getPageSize();
    int pageNum = userListRequest.getPageNum();
    String keyword = userListRequest.getKeyword();
    Integer status = userListRequest.getStatus();
    int offset = (pageNum - 1) * pageSize;
    List<User> users = userMapper.selectUserList(offset, pageSize, keyword, status);
    long total = getUserListTotal(keyword, status);
    
    PageResponse<UserResponse> userListResponse = PageResponse.<UserResponse>builder()
      .pageSize(pageSize)
      .pageNum(pageNum)
      .data(users.stream().map(this::convertToUserResponse).collect(Collectors.toList()))
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
    User user = requireUser(userId);

    userMapper.updateUserStatus(userId, status);
    updateUserStatusInRedis(userId, status);
  }

  @Override
  public void updateUserStatusInRedis(Long userId, Integer status) {
    blackListService.addUserStatusToRecord(userId, status);
  }

  private User requireUser(Long userId) {
    User user = userMapper.selectByUserId(userId);
    if (user == null) {
      throw new BusinessException(ExceptionCodeEnum.UserNotFound);
    }
    return user;
  }
}