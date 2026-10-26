package com.zhuyue.clockMyGoal.modules.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.service.AdminUserService;

@RestController
@RequestMapping("/admin/admins")
@Validated
public class AdminUserController {
  @Autowired
  AdminUserService adminUserService;

  @GetMapping("/{id}")
  public AdminUserResponse getAdminUserById(@PathVariable("id") Long id) {
    return adminUserService.getAdminUserById(id);
  }
}
