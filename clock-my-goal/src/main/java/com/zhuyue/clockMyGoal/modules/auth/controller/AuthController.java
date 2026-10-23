package com.zhuyue.clockMyGoal.modules.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.zhuyue.clockMyGoal.common.authentication.IgnoreJwt;
import com.zhuyue.clockMyGoal.modules.auth.dto.request.LoginDto;
import com.zhuyue.clockMyGoal.modules.auth.dto.request.RegisterDto;
import com.zhuyue.clockMyGoal.modules.auth.dto.response.LoginResponse;
import com.zhuyue.clockMyGoal.modules.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired 
  private AuthService authService;

  @IgnoreJwt
  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginDto request) {
    return authService.login(request);
  }

  @IgnoreJwt
  @PostMapping("/register")
  public Long register(@RequestBody RegisterDto request) {
    return authService.register(request);
  }

  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    authService.logout(authHeader);
  }
}