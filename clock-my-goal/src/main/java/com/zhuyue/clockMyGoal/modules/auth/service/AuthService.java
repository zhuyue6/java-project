package com.zhuyue.clockMyGoal.modules.auth.service;

import com.zhuyue.clockMyGoal.modules.auth.dto.request.LoginDto;
import com.zhuyue.clockMyGoal.modules.auth.dto.request.RegisterDto;
import com.zhuyue.clockMyGoal.modules.auth.dto.response.LoginResponse;

public interface AuthService {
  public LoginResponse login(LoginDto request);
  public void logout(String authHeader);
  public Long register(RegisterDto request);
}
