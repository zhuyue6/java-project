package com.zhuyue.clockin.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zhuyue.clockin.common.util.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({ BusinessException.class, BaseException.class })
  public ApiResponse<Object> handleBusinessException(BusinessException e) {
    ApiResponse<Object> result = ApiResponse.fail(e.getMessage(), e.getCode());
    return result;
  }
}
