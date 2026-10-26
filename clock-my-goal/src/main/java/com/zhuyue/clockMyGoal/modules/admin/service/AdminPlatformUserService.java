package com.zhuyue.clockMyGoal.modules.admin.service;

import com.zhuyue.clockMyGoal.modules.admin.dto.request.PlatformUserListRequest;
import com.zhuyue.clockMyGoal.modules.user.dto.response.PageResponse;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.PlatformUserResponse;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;

public interface AdminPlatformUserService {
  PlatformUserResponse getUserById(long id);
  PageResponse<UserResponse> getUserList(PlatformUserListRequest userListRequest);
  void updateUserStatus(long id, int status);
} 
