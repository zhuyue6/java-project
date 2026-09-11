package com.zhuyue.clockin.modules.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
  private Long id;
  private String userName;
  private int age;
  private Integer sex;
  private String token;
  private String avatarUrl;
}

