package com.zhuyue.clockMyGoal.modules.user.dto;

import lombok.Data;

@Data
public class UserCredential {
  private Long id;
  private String userName;
  private String password;
  private Integer status;
}
