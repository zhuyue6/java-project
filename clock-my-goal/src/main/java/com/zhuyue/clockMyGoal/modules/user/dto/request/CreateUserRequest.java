package com.zhuyue.clockMyGoal.modules.user.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
  private String userName;
  private String password;
  private Integer sex;
  private Integer age;
}
