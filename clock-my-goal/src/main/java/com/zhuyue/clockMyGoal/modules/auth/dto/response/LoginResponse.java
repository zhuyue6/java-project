package com.zhuyue.clockMyGoal.modules.auth.dto.response;

import com.zhuyue.clockMyGoal.modules.user.dto.response.UserResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends UserResponse {
  private String token;
}
