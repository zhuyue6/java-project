package com.zhuyue.clockMyGoal.modules.admin.service;

import com.zhuyue.clockMyGoal.modules.admin.dto.request.AdminUserLoginRequest;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserLoginResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.UserInfoResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.request.UserListRequest;

public interface AdminService {
  AdminUserLoginResponse login(AdminUserLoginRequest request);
  AdminUserResponse getAdminUserInfo(Long id);
  void logout(String authHeader);
  PageResponse<UserResponse> getUserList(UserListRequest request);
  UserInfoResponse getUserInfo(long id);
  void updateUserStatus(Long id, Integer status);
}
