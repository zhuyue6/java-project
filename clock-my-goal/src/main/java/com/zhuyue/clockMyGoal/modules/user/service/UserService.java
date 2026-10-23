package com.zhuyue.clockMyGoal.modules.user.service;

import org.springframework.web.multipart.MultipartFile;

import com.zhuyue.clockMyGoal.modules.user.dto.request.*;
import com.zhuyue.clockMyGoal.modules.user.dto.response.*;

/**
 * 用户服务接口
 * UserService
 */
public interface UserService {
  // 登录接口
  UserLoginReponse login (UserLoginRequest dto);
  // 注册接口
  long register(UserRegisterRequest dto);
  // 登出接口
  void logout(String authHeader);
  // 获取用户详细
  UserResponse getUserByUserId(Long userId);
  // 上传头像接口
  String uploadAvatar(long userId, MultipartFile file);
  // 更新用户状态
  void updateUserStatus(Long userId, Integer status);
  // 更新用户状态到redis
  void updateUserStatusInRedis(Long userId, Integer status);
  // 获取用户列表
  PageResponse<UserResponse> getUserList(UserListRequest userListRequest);
  // 获取用户列表总数
  long getUserListTotal(String keyword, Integer status);
}
