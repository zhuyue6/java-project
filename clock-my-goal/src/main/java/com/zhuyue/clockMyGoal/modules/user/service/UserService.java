package com.zhuyue.clockMyGoal.modules.user.service;

import org.springframework.web.multipart.MultipartFile;

import com.zhuyue.clockMyGoal.modules.user.dto.response.*;
import com.zhuyue.clockMyGoal.modules.user.dto.request.CreateUserRequest;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserCredentialRequest;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserListRequest;

/**
 * 用户服务接口
 * UserService
 */
public interface UserService {
  // 根据用户名查询用户
  UserCredentialRequest getUserCredentialByUserName(String userName);
  // 获取用户详细
  UserResponse getUserByUserId(Long userId);
  // 上传头像接口
  String uploadUserAvatar(long userId, MultipartFile file);
  // 更新用户状态
  void updateUserStatus(Long userId, Integer status);
  // 更新用户状态到redis
  void updateUserStatusInRedis(Long userId, Integer status);
  // 获取用户列表
  PageResponse<UserResponse> getUserList(UserListRequest userListRequest);
  // 获取用户列表总数
  long getUserListTotal(String keyword, Integer status);
  // 注册用户
  long createUser(CreateUserRequest dto);
}
