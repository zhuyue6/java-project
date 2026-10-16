package com.zhuyue.clockMyGoal.common.exception;

import java.io.IOException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessIOException extends IOException {
  private int code;
  private String message;
}
