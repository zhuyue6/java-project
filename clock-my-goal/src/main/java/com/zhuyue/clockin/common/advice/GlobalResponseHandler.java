package com.zhuyue.clockin.common.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.zhuyue.clockin.common.util.ApiResponse;
import com.zhuyue.clockin.common.util.UtilService;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object>{
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    // 排除情况1：方法或类上有 @IgnoreApiResponse 注解
    if (returnType.hasMethodAnnotation(IgnoreApiResponse.class) 
            || returnType.getContainingClass().isAnnotationPresent(IgnoreApiResponse.class)) {
      return false;
    }

    // 排除情况2：返回值已经是 ApiResponse 类型（避免重复包装）
    return !returnType.getParameterType().equals(ApiResponse.class);
  }

  @Override
  public Object beforeBodyWrite(
    Object body,
    MethodParameter returnType,
    MediaType selectedContentType,
    Class<? extends HttpMessageConverter<?>> selectedConverterType,
    ServerHttpRequest request,
    ServerHttpResponse response
  ) {
    // 运行时已是 ApiResponse，避免异常处理结果再被包一层成功
    if (body instanceof ApiResponse) {
      return body;
    }

    ApiResponse<?> apiResponse = ApiResponse.success(body);

    // 方法返回 String 时 Spring 已选定 StringHttpMessageConverter，
    // 直接返回 ApiResponse 会触发 ClassCastException，需手动序列化为 JSON 字符串
    if (returnType.getParameterType().equals(String.class)) {
      response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
      return UtilService.objectToString(apiResponse);
    }

    return apiResponse;
  }
}