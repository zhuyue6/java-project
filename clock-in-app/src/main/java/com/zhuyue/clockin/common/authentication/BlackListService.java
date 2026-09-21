package com.zhuyue.clockin.common.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


// 黑名单服务
@Service
public class BlackListService {
  @Autowired
  private StringRedisTemplate redisTemplate;
  private static final String TOKEN_BLACKLIST_PREFIX = "jwt:blacklist:";

  public void addTokenToBlacklist(String token, long remainSeconds) {
    // 将token加入黑名单，并设置过期时间, 并触发TTL（time to live）
    redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "true", remainSeconds, TimeUnit.SECONDS);
  }
  
  public boolean isTokenInBlacklist(String token) {
    return redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
  }

}
