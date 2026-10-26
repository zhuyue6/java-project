package com.zhuyue.clockMyGoal.modules.admin.service;

import com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserLoginResponse;

public interface AdminAuthService {
  AdminUserLoginResponse login(AdminUserLoginRequest request);
  void logout(String authHeader);
}
