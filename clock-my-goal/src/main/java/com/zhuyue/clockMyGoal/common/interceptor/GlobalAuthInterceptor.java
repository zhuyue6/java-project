package com.zhuyue.clockMyGoal.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.zhuyue.clockMyGoal.common.authentication.*;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.modules.admin.entity.AdminUser;
import com.zhuyue.clockMyGoal.modules.admin.mapper.AdminUserMapper;
import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;
import com.zhuyue.clockMyGoal.modules.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Objects;


@Component
public class GlobalAuthInterceptor implements HandlerInterceptor {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private BlackListService blackListService;

  @Autowired
  private UserService userService;

  @Autowired
  private AdminUserMapper adminUserMapper;

  @Override 
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 非 Controller 方法（静态资源等）直接放行
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }
    // 方法或类上标注 @IgnoreJwt 的接口跳过鉴权（如登录/注册）
    if (handlerMethod.hasMethodAnnotation(IgnoreJwt.class)
        || handlerMethod.getBeanType().isAnnotationPresent(IgnoreJwt.class)) {
      return true;
    }

    // 获取请求头中的Authorization字段
    String authHeader = request.getHeader("Authorization");
    BusinessException UnauthorizedException = new BusinessException(ExceptionCodeEnum.Unauthorized);
    // 如果未授权，则返回401状态码和错误信息
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw UnauthorizedException;
    }

    // 获取token
    String token = jwtService.getToken(authHeader);
    BusinessException LoginExpiredException = new BusinessException(ExceptionCodeEnum.LoginExpired);

    // 如果token在黑名单中，则返回401状态码和错误信息
    if (blackListService.isTokenInBlacklist(token)) {
      throw LoginExpiredException;
    }

    // 解析token, 如果过期直接抛出异常
    Map<String, Object> claims = null;
    try {
      claims = jwtService.parseToken(token);
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw LoginExpiredException;
    }
    Long userId = (Long) claims.get("id");
    String accountType = String.valueOf(claims.get("accountType"));
    LoginUser loginUser = new LoginUser(userId, (String) claims.get("userName"));

    // 判断api 前置来源是否为管理平台
    String path = request.getServletPath();
    boolean adminPath = path != null && path.startsWith("/admin");

    // 对admin api进行鉴权
    if ("admin".equals(accountType)) {
      if (!adminPath) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        throw UnauthorizedException;
      }
      AdminUser adminUser = adminUserMapper.selectAdminUserById(userId);
      if (adminUser == null || Objects.equals(adminUser.getStatus(), 0)) {
        throw LoginExpiredException;
      }
      request.setAttribute("userInfo", loginUser);
      return true;
    }

    if (adminPath) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw UnauthorizedException;
    }

    // redis 没有状态时回源数据库，停用后下一次请求就能拦住
    Integer userStatus = blackListService.getUserStatus(userId);
    if (userStatus == null) {
      UserResponse user = userService.getUserByUserId(userId);
      if (user == null) {
        throw LoginExpiredException;
      }
      userStatus = user.getStatus();
      if (userStatus != null) {
        userService.updateUserStatusInRedis(userId, userStatus);
      }
    }
    if (Objects.equals(userStatus, 0)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      throw new BusinessException(ExceptionCodeEnum.UserStatusDisabled);
    }

    // 将用户信息存储到请求中
    request.setAttribute("userInfo", loginUser);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // 返回类型为json/UTF-8
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
  }
}
