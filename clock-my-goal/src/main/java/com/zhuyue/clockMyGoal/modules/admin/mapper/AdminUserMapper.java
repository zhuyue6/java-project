package com.zhuyue.clockMyGoal.modules.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhuyue.clockMyGoal.modules.admin.entity.AdminUser;
import java.util.List;

@Mapper
public interface AdminUserMapper {
  AdminUser selectAdminUserByUserName(@Param("userName") String userName);
  void insertAdminUser(AdminUser adminUser);
  void updateAdminUser(AdminUser adminUser);
  void deleteAdminUser(@Param("id") Long id);
  AdminUser selectAdminUserById(@Param("id") Long id);
  List<AdminUser> selectAllAdminUsers();
}