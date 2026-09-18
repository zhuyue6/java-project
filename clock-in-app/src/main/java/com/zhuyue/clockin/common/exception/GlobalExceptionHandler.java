package com.zhuyue.clockin.common.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zhuyue.clockin.common.constants.ExceptionCodeEnum;
import com.zhuyue.clockin.common.util.ApiResponse;
import java.lang.Exception;

@RestControllerAdvice
public class GlobalExceptionHandler {
  // 捕获业务异常
  @ExceptionHandler(BusinessException.class)
  public ApiResponse<Object> handleBusinessException(BusinessException e) {
    return ApiResponse.fail(e.getMessage(), e.getCode());
  }

  // @Valid @RequestBody 校验失败
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiResponse<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    // 获取第一个错误信息
    // 
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getDefaultMessage())
        .findFirst()
        .orElseGet(() -> e.getBindingResult().getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse(ExceptionCodeEnum.InvalidParameters.getMessage()));
    return ApiResponse.fail(message, ExceptionCodeEnum.InvalidParameters.getCode());
  }

  // JSON 解析失败 / body 为空 / Content-Type 不对 —— 进不了 @Valid，会抛这个
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ApiResponse<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
    return ApiResponse.fail(
        ExceptionCodeEnum.InvalidParameters.getMessage(),
        ExceptionCodeEnum.InvalidParameters.getCode());
  }

  @ExceptionHandler(Exception.class)
  public ApiResponse<Object> handleException(Exception e) {
    // 捕获全部异常，返回服务错误
    return ApiResponse.fail(e.getMessage(), ExceptionCodeEnum.ServiceError.getCode());
  }
}
