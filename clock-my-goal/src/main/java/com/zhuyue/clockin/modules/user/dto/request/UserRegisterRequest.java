package com.zhuyue.clockin.modules.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterRequest {
  @NotBlank(message = "名称必须填写")
  @Size(min = 1, max = 10, message = "名称长度必须在1到10之间")
  @Pattern(
    regexp = "^[\\u4e00-\\u9fa5a-zA-Z·]+$",
    message = "姓名只能包含中文、字母和间隔号·"
  )
  private String userName;

  @NotBlank(message = "密码必须填写")
  @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
  @Pattern(
    regexp = "^[a-zA-Z0-9_+\\-]+$",
    message = "密码只能包含数字大小写字母_-+"
  )
  private String password;

  @Min(value = 1, message = "性别只能传男、女和保密其中一项")
  @Max(value = 3, message = "性别只能传男、女和保密其中一项")
  private Integer sex;

  @Min(value = 0, message = "年龄不能小于0")
  @Max(value = 150, message = "年龄不能大于150")
  private int age;
}
