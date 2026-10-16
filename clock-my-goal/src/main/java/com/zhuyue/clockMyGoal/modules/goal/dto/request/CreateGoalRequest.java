package com.zhuyue.clockMyGoal.modules.goal.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateGoalRequest {
  private String remark;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private LocalDateTime expirationTime;
  private long labelId;
  // @Min(value = 1, message = "目标打卡次数不能小于1")
  // private int targetClockInCount;
  @Min(value = 1, message = "打卡周期不能小于1")
  @Max(value = 3, message = "打卡周期不能大于3")
  private int clockInCycle;
}
