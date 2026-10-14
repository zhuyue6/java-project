package com.zhuyue.clockin.common.interceptor;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginUser {
  private Long userId;
  private String userName;
}
