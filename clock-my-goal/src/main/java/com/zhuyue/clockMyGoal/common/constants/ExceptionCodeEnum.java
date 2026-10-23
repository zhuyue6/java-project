package com.zhuyue.clockMyGoal.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCodeEnum {
  ServiceError(10001, "服务器系统错误"),
  NotFound(10002, "不存在"),
  AlreadyExists(10003, "已存在"),
  Unauthorized(10004, "未授权，请先登录"),
  LoginExpired(10005, "登录已过期，请重新登录"),
  InvalidParameters(10006, "参数错误"),

  // 业务错误 (11xxx)
  // 用户错误 (111xx)
  UserNotFound(11001, "用户不存在"),
  UserNameOrPasswordError(11002, "用户名或密码错误"),
  UserAlreadyExists(11003, "用户已存在"),
  UserStatusDisabled(11004, "该账号已被停用，暂时无法使用"),

  // 文件错误 (12xxx)
  InvalidMimeType(12001, "文件类型错误"),
  FileUploadError(12002, "文件上传失败"),

  // 标签错误 (13xxx)
  LabelNameAlreadyExists(13001, "该标签分类下名称已存在"),
  LabelNotFound(13002, "标签不存在"),

  // 目标错误（14xxx）
  GoalNotFound(14001, "目标不存在"),

  // 管理员错误 (15xxx)
  AdminUserNotFound(15001, "用户不存在"),
  AdminUserNameOrPasswordError(15002, "用户名或密码错误"),
  AdminUserAlreadyExists(15003, "用户已存在"),
  AdminUserStatusDisabled(15004, "管理员账号已停用");

  /** 错误码 */
  private int code;
  /** 错误信息 */
  private String message;
}