package com.zhuyue.clockin.common.exception;

import java.io.IOException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessIOException extends IOException {
  private int code = -1;
  private String message;
  public BusinessIOException(String message) {
    this.message = message;
  }
}
