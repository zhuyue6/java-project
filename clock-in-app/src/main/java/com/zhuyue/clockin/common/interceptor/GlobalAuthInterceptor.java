package com.zhuyue.clockin.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.zhuyue.clockin.common.authentication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Component;
import com.zhuyue.clockin.common.util.ApiResponse;
import com.zhuyue.clockin.common.constants.ExceptionCodeEnum;
import org.springframework.http.MediaType;
import java.util.Map;
import io.jsonwebtoken.ExpiredJwtException;
import com.zhuyue.clockin.common.util.UtilService;

// 这里

@Component
public class GlobalAuthInterceptor implements HandlerInterceptor {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private BlackListService blackListService;

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

    // 如果未授权，则返回401状态码和错误信息
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      ApiResponse<Object> failedResponse = ApiResponse.fail(
          ExceptionCodeEnum.Unauthorized.getMessage(),
          ExceptionCodeEnum.Unauthorized.getCode());
      response.getWriter().write(UtilService.objectToString(failedResponse));
      return false;
    }

    // 获取token
    String token = jwtService.getToken(authHeader);
    ApiResponse<Object> failedResponse = ApiResponse.fail(ExceptionCodeEnum.LoginExpired.getMessage(), ExceptionCodeEnum.LoginExpired.getCode());
    String failedResponseString = UtilService.objectToString(failedResponse);

    // 如果token在黑名单中，则返回401状态码和错误信息
    if (blackListService.isTokenInBlacklist(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(failedResponseString);
      return false;
    }

    try {
      // 将用户信息存储到请求中
      Map<String, Object> claims = jwtService.parseToken(token);
      LoginUser loginUser = new LoginUser((Long) claims.get("id"), (String) claims.get("userName"));
      request.setAttribute("userInfo", loginUser);
      return true;
    } catch (ExpiredJwtException e) {
      response.getWriter().write(failedResponseString);
      return false;
    } catch (Exception e) {
      response.getWriter().write(failedResponseString);
      return false;
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // 返回类型为json/UTF-8
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
  }
}
