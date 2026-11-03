package com.zhuyue.clockMyGoal.modules.goal.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.*;

@Data
public class UpdateGoalRequest {
  @NotBlank(message = "目标名称不能为空")
  private String goalName;

  private String remark;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime expirationTime;

  @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
  private LocalTime startCheckTime;

  @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
  private LocalTime endCheckTime;

  private Integer targetClockInCycleCount;

  private Long labelId;

  @Min(value = 1, message = "打卡周期不能小于1")
  @Max(value = 3, message = "打卡周期不能大于3")
  private Integer clockInCycle;
}
