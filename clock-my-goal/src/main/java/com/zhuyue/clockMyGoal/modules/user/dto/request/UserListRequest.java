package com.zhuyue.clockMyGoal.modules.user.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserListRequest extends PageRequest {
  private String keyword;
  private Integer status;
}