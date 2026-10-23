package com.zhuyue.clockMyGoal.modules.user.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserListRequest extends PageRequest {
  private String keyword;
  
  @Min(value = 0, message = "状态不能小于0")
  @Max(value = 1, message = "状态不能大于1")
  private Integer status;
}