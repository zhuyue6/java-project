package com.zhuyue.clockMyGoal.modules.goal.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateGoalRequest {
  @NotBlank(message = "目标名称不能为空")
  @NotNull(message = "目标名称不能为空")
  private String goalName;

  private String remark;

  @NotNull(message = "过期时间不能为空")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime expirationTime;

  @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
  private LocalTime startCheckTime;

  @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
  private LocalTime endCheckTime;

  @Min(value = 1, message = "打卡时间周期计数不能小于1")
  private Integer targetClockInCycleCount;

  private Long labelId;

  @Min(value = 1, message = "打卡周期不能小于1")
  @Max(value = 3, message = "打卡周期不能大于3")
  private int clockInCycle;
}
