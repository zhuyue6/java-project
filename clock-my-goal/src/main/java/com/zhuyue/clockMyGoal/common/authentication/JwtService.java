package com.zhuyue.clockMyGoal.common.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.*;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import javax.crypto.SecretKey;
import java.util.Date;

@Service("JwtService")
public class JwtService {
  // jwt秘钥
  @Value("${jwt.secret}")
  private String secret;
  // jwt过期时间
  @Value("${jwt.expiration}")
  private long expiration;

  // 创建签名密钥
  private SecretKey createSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  // 生成小程序用户令牌
  public String generateToken(Long userId, String username) {
    return generateToken(userId, username, "user");
  }

  // accountType：user 小程序用户，admin 管理员
  public String generateToken(Long userId, String username, String accountType) {
    return Jwts.builder()
      .subject(userId.toString())
      .claim("userName", username)
      .claim("accountType", accountType)
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(createSecretKey(), Jwts.SIG.HS256)
      .compact();
  }

  // 解析令牌
  public Map<String, Object> parseToken(String token) throws ExpiredJwtException {
    Jws<Claims> jws = Jwts.parser()
      // 验签密钥
      .verifyWith(createSecretKey())
      // 构建解析器
      .build()
      // 校验签名
      .parseSignedClaims(token);

      Map<String, Object> claims = new HashMap<>();
      // JWT subject 是字符串，这里转成 Long 供鉴权拦截器使用
      Object accountType = jws.getPayload().get("accountType");
      claims.put("id", Long.valueOf(jws.getPayload().getSubject()));
      claims.put("userName", jws.getPayload().get("userName"));
      claims.put("accountType", accountType == null ? "user" : accountType.toString());
      claims.put("exp", jws.getPayload().getExpiration());
      return claims;
  }

  // 获取token剩余过期秒数
  public long getTokenRemainSeconds(String token) {
    Map<String, Object> claims = parseToken(token);
    Date exp = (Date) claims.get("exp");
    if (exp == null) {
      return 0;
    }
    long expTime = exp.getTime();
    return (expTime - System.currentTimeMillis()) / 1000; // 转换为秒
  }

  public String getToken(String authHeader) {
    String token = authHeader.substring(7);
    return token;
  }
}
