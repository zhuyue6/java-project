package com.zhuyue.clockMyGoal.modules.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginReponse {
  private UserResponse user;
  private String token;
}
