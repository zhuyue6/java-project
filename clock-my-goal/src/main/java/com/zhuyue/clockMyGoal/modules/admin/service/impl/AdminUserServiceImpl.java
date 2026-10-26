package com.zhuyue.clockMyGoal.modules.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuyue.clockMyGoal.modules.admin.service.AdminUserService;
import com.zhuyue.clockMyGoal.modules.admin.mapper.AdminUserMapper;
import com.zhuyue.clockMyGoal.modules.admin.dto.response.AdminUserResponse;
import com.zhuyue.clockMyGoal.modules.admin.entity.AdminUser;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockMyGoal.common.util.TimeService;

@Service("AdminUserService")
public class AdminUserServiceImpl implements AdminUserService {
  @Autowired
  private AdminUserMapper adminUserMapper;

  AdminUserResponse converToAdminUserResponse(AdminUser adminUser) {
    String createTime = TimeService.getTimeStringByTime(adminUser.getCreateTime());
    String updateTime = TimeService.getTimeStringByTime(adminUser.getUpdateTime());

    AdminUserResponse adminUserResponse = new AdminUserResponse();
    adminUserResponse.setId(adminUser.getId());
    adminUserResponse.setUserName(adminUser.getUserName());
    adminUserResponse.setRole(adminUser.getRole());
    adminUserResponse.setStatus(adminUser.getStatus());
    adminUserResponse.setCreateTime(createTime);
    adminUserResponse.setUpdateTime(updateTime);
    return adminUserResponse;
  }


  @Override
  public AdminUserResponse getAdminUserById(Long id) {
    AdminUser adminUser = adminUserMapper.selectAdminUserById(id);
    if (adminUser == null) {
      throw new BusinessException(ExceptionCodeEnum.AdminUserNotFound);
    }
    return converToAdminUserResponse(adminUser);
  }
  
  @Override
  public AdminUser getAdminUserByUserName(String userName) {
    return adminUserMapper.selectAdminUserByUserName(userName);
  }
}
