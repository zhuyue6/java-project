package com.zhuyue.clockMyGoal.modules.admin.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuyue.clockMyGoal.modules.admin.service.AdminAuthService;
import com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserLoginResponse;
import com.zhuyue.clockMyGoal.modules.admin.entity.AdminUser;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockMyGoal.common.authentication.JwtService;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.common.authentication.BlackListService;
import com.zhuyue.clockMyGoal.modules.admin.service.AdminUserService;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;


@Service("AdminAuthService")
public class AdminAuthServiceImpl implements AdminAuthService {

  @Autowired 
  private BlackListService blackListService;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AdminUserService adminUserService;

  @Override
  public AdminUserLoginResponse login(AdminUserLoginRequest dto) {
    AdminUser adminUser = adminUserService.getAdminUserByUserName(dto.getUserName());

    if (adminUser == null) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserNotFound);
    }

    if (!adminUser.getPassword().equals(dto.getPassword())) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserNameOrPasswordError);
    }

    if (Objects.equals(adminUser.getStatus(), 0)) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserStatusDisabled);
    }

    long id = adminUser.getId();

    String token = jwtService.generateToken(id, adminUser.getUserName(), "admin");

    AdminUserResponse adminUserResponse = adminUserService.getAdminUserById(id);

    AdminUserLoginResponse adminUserLoginResponse = new AdminUserLoginResponse();
    adminUserLoginResponse.setId(adminUserResponse.getId());
    adminUserLoginResponse.setUserName(adminUserResponse.getUserName());
    adminUserLoginResponse.setCreateTime(adminUserResponse.getCreateTime());
    adminUserLoginResponse.setUpdateTime(adminUserResponse.getUpdateTime());
    adminUserLoginResponse.setToken(token);
    adminUserLoginResponse.setStatus(adminUserResponse.getStatus());
    adminUserLoginResponse.setRole(adminUserResponse.getRole());
    return adminUserLoginResponse;
  }

  @Override
  public void logout(String authHeader) {
    String token = jwtService.getToken(authHeader);
    long remainSeconds = jwtService.getTokenRemainSeconds(token);
    if (remainSeconds > 0) {
      blackListService.addTokenToBlacklist(token, remainSeconds);
    }
  }
}
