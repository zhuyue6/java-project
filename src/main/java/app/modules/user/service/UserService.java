package app.modules.user.service;

import app.modules.user.dto.request.UserLoginRequest;
import app.modules.user.dto.request.UserRegisterRequest;
import app.modules.user.dto.request.UserWxLoginRequest;
import app.modules.user.dto.response.*;

public interface UserService {
  // 登录接口
  UserResponse login (UserLoginRequest dto);
  UserResponse wxLogin(UserWxLoginRequest dto);
  // 注册接口
  void register(UserRegisterRequest dto);
}
