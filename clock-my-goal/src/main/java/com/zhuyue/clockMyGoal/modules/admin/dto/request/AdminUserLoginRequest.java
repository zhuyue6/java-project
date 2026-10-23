package com.zhuyue.clockMyGoal.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class AdminUserLoginRequest {
  @NotBlank(message = "用户名不能为空")
  @Size(min = 1, max = 10, message = "名称长度必须在1到10之间")
  @Pattern(
    regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9·]+$",
    message = "姓名只能包含中文、字母、数字和间隔号·"
  )
  private String userName;
  @NotBlank(message = "密码不能为空")
  @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
  @Pattern(
    regexp = "^[a-zA-Z0-9_+\\-]+$",
    message = "密码只能包含数字大小写字母_-+"
  )
  private String password;
}
