package com.zhuyue.clockMyGoal.common.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.Objects;


// 黑名单服务
@Service
public class BlackListService {
  @Autowired
  private StringRedisTemplate redisTemplate;
  private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
  private static final String USER_STATUS_PREFIX = "user:status:";

  public void addTokenToBlacklist(String token, long remainSeconds) {
    // 将token加入黑名单，并设置过期时间, 并触发TTL（time to live）
    redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "true", remainSeconds, TimeUnit.SECONDS);
  }
  
  public boolean isTokenInBlacklist(String token) {
    return redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
  }
  
  public void addUserStatusToRecord(Long userId, Integer status) {
    if (status == null) {
      return;
    }

    String statusString = status.toString();
    redisTemplate.opsForValue().set(USER_STATUS_PREFIX + userId, Objects.requireNonNull(statusString), 10, TimeUnit.MINUTES);
  }

  public Integer getUserStatus(Long userId) {
    String status = redisTemplate.opsForValue().get(USER_STATUS_PREFIX + userId);
    if (status == null) {
      return null;
    }
    return Integer.parseInt(status);
  }
}
