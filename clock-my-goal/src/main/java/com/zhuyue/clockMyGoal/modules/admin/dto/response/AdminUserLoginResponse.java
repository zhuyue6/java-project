package com.zhuyue.clockMyGoal.modules.admin.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserLoginResponse extends AdminUserResponse {
  /**
   * 令牌
   */
  private String token;
}
