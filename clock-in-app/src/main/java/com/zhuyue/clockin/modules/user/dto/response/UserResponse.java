package com.zhuyue.clockin.modules.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
  private long id;
  private String userName;
  private int age;
  private int sex;
  private String token;
  private String avatarUrl;
}

