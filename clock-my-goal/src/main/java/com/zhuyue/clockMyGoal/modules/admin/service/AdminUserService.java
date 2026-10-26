package com.zhuyue.clockMyGoal.modules.admin.service;

import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.entity.AdminUser;

public interface AdminUserService {
  AdminUserResponse getAdminUserById(Long id);
  AdminUser getAdminUserByUserName(String userName);
}
