package app.common.filter;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import app.common.interceptor.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  public ApiResponse<Object> handleBusinessException(BusinessException e) {
    ApiResponse<Object> result = ApiResponse.fail(e.getMessage(), e.getCode());
    return result;
  }
}
