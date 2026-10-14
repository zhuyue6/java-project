package com.zhuyue.clockin.modules.user.service;

import com.zhuyue.clockin.modules.user.dto.request.*;
import com.zhuyue.clockin.modules.user.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 * UserService
 */
public interface UserService {
  // 登录接口
  UserResponse login (UserLoginRequest dto);
  // 注册接口
  long register(UserRegisterRequest dto);
  // 登出接口
  void logout(String authHeader);
  // 上传头像接口
  String uploadAvatar(long userId, MultipartFile file);
}
