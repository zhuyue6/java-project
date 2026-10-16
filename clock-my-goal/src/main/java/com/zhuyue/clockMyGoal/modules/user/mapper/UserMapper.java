package com.zhuyue.clockMyGoal.modules.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhuyue.clockMyGoal.modules.user.entity.User;

/**
 * 用户Mapper接口
 * UserMapper
 */
@Mapper // 标记为MyBatis Mapper接口，被Spring扫描
public interface UserMapper {
  // @param 注解标注参数名，防止参数名与属性名不一致时，MyBatis无法正确映射
  // 根据ID查询用户
  User selectByUserId(@Param("id") Long id);
  // 根据Name查询用户
  User selectByUserName(@Param("userName") String userName);
  // insert 返回影响行数；自增 id 会写回 user.getId()
  void insertUser(User user);
  void updateAvatarByUserId(@Param("id") Long id, @Param("avatarUrl") String avatarUrl);
}