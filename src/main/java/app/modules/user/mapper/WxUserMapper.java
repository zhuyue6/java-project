package app.modules.user.mapper;

import app.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 标记为MyBatis Mapper接口，被Spring扫描
public interface WxUserMapper extends UserMapper {
  // 根据openid查询用户
  User selectByOpenId(String openid);
}