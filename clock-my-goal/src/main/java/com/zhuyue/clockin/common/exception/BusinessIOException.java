package com.zhuyue.clockin.common.exception;

import java.io.IOException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessIOException extends IOException {
  private int code;
  private String message;
}
