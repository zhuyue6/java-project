package com.zhuyue.clockMyGoal.modules.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserLoginResponse;
import com.zhuyue.clockMyGoal.modules.admin.service.AdminAuthService;

@RestController
@RequestMapping("/admin/auth") 
@Validated
public class AdminAuthController {
  @Resource(name = "AdminAuthService")
  AdminAuthService adminAuthService;

  @PostMapping("/login")
  public AdminUserLoginResponse login(@Valid @RequestBody AdminUserLoginRequest request) {
    return adminAuthService.login(request);
  }

  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    adminAuthService.logout(authHeader);
  }
}
