package com.zhuyue.clockMyGoal.modules.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhuyue.clockMyGoal.common.authentication.IgnoreJwt;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.UpdateUserStatusRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserLoginResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.UserInfoResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserListRequest;

import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.admin.service.AdminService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {
  @Resource(name = "AdminService")
  AdminService adminService;
  @IgnoreJwt
  @PostMapping("/login")
  public AdminUserLoginResponse login(@Valid @RequestBody AdminUserLoginRequest request) {
    return adminService.login(request);
  }

  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    adminService.logout(authHeader);
  }

  @GetMapping("/getAdminUserInfo/{id}")
  public AdminUserResponse getAdminUserInfo(@Valid @PathVariable("id") long id) {
    return adminService.getAdminUserInfo(id);
  }

  @PostMapping("/getUserList")
  public PageResponse<UserResponse> getUserList(@Valid @RequestBody UserListRequest request) {
    return adminService.getUserList(request);
  }

  @GetMapping("/getUserInfo/{id}")
  public UserInfoResponse getUserInfo(@PathVariable("id") Long id) {
    return adminService.getUserInfo(id);
  }

  @PostMapping("/updateUserStatus")
  public void updateUserStatus(
      @Valid @RequestBody UpdateUserStatusRequest request) {
    Long id = request.getId();
    Integer status = request.getStatus();
    adminService.updateUserStatus(id, status);
  }
}
