package app.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCodeEnum {
  NotFound(10004, "不存在"),
  AlreadyExists(10005, "已存在"),

  // 业务错误 (11xxx)
  // 用户错误 (111xx)
  UserNotFound(11000, "用户不存在"),
  UserNameOrPasswordError(11002, "用户名或密码错误"),
  UserAlreadyExists(10005, "用户已存在");
  
  private int code;
  private String message;
}
