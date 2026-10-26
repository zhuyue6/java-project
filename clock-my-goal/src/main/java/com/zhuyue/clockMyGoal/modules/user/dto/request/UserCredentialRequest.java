package com.zhuyue.clockMyGoal.modules.user.dto.request;

import lombok.Data;

@Data
public class UserCredentialRequest {
  private Long id;
  private String userName;
  private String password;
  private Integer status;
}
