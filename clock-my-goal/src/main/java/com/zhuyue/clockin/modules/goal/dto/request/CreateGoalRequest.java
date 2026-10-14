package com.zhuyue.clockin.modules.goal.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.zhuyue.clockin.common.validate.DateFormat;

@Data
public class CreateGoalRequest {
  private String remark;
  @DateFormat
  @NotNull(message = "打卡时间不能为空")
  private String clockInTime;
  @DateFormat
  private String clockOutTime;
  private long labelId;
  @Min(value = 1, message = "目标打卡次数不能小于1")
  private int targetClockInCount;
  @Min(value = 1, message = "打卡周期不能小于1")
  @Max(value = 3, message = "打卡周期不能大于3")
  private int clockInCycle;
}
