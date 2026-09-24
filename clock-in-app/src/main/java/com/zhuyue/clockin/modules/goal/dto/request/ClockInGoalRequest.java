package com.zhuyue.clockin.modules.goal.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data 
public class ClockInGoalRequest {
  @NotNull(message = "目标ID不能为空")
  private long id;
}
