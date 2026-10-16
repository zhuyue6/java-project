package com.zhuyue.clockMyGoal.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.zhuyue.clockMyGoal.common.constants.ExceptionCodeEnum;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends BaseException {
  private int code;
  private String message;
  public BusinessException(int code, String message) {
    this.code = code;
    this.message = message;
  }
  public BusinessException(ExceptionCodeEnum exceptionCodeEnum) {
    this.code = exceptionCodeEnum.getCode();
    this.message = exceptionCodeEnum.getMessage();
  }
}
