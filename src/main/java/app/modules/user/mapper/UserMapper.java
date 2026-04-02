package app.modules.user.mapper;

import app.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 标记为MyBatis Mapper接口，被Spring扫描
public interface UserMapper {
  // 根据ID查询用户
  User selectById(Long id);
  // 根据Name查询用户
  User selectByName(String name);
}