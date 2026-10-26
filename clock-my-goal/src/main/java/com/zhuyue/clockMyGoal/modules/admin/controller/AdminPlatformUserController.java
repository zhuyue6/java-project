package com.zhuyue.clockMyGoal.modules.admin.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.zhuyue.clockMyGoal.modules.admin.service.AdminPlatformUserService;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.PlatformUserListRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.PlatformUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.UpdatePlatformUserStatusRequest;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;

@RestController
@RequestMapping("/admin/users")
@Validated
public class AdminPlatformUserController {
  @Autowired 
  AdminPlatformUserService adminPlatformUserService;

  @GetMapping("/{id}")
  public PlatformUserResponse getUserById(@PathVariable("id") Long id) {
    return adminPlatformUserService.getUserById(id);
  }

  @GetMapping
  public PageResponse<UserResponse> getUserList(@Valid PlatformUserListRequest request) {
    return adminPlatformUserService.getUserList(request);
  }

  @PostMapping("/{id}/status")
  public void updateUserStatus(@PathVariable("id") long id, @Valid @RequestBody UpdatePlatformUserStatusRequest request) {
    adminPlatformUserService.updateUserStatus(id, request.getStatus());
  }
}
