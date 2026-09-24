package com.zhuyue.clockin.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCodeEnum {
  ServiceError(10001, "服务器系统错误"),
  NotFound(10004, "不存在"),
  AlreadyExists(10005, "已存在"),
  Unauthorized(10006, "未授权，请先登录"),
  LoginExpired(10007, "登录已过期，请重新登录"),
  InvalidParameters(10008, "参数错误"),

  // 业务错误 (11xxx)
  // 用户错误 (111xx)
  UserNotFound(11100, "用户不存在"),
  UserNameOrPasswordError(11102, "用户名或密码错误"),
  UserAlreadyExists(11105, "用户已存在"),

  // 文件错误 (12xxx)
  InvalidMimeType(12000, "文件类型错误"),
  FileUploadError(12001, "文件上传失败"),

 // 标签错误 (13xxx)
 LabelNameAlreadyExists(13000, "该标签分类下名称已存在"),

 // 目标错误（14xxx）
 GoalNotFound(14000, "目标不存在");


  private int code;
  private String message;
}