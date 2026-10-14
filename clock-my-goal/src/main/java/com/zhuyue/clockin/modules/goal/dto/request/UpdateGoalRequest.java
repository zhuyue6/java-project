package com.zhuyue.clockin.modules.goal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateGoalRequest extends CreateGoalRequest {
  @NotNull(message = "目标id不能为空")
  private long id;
}
