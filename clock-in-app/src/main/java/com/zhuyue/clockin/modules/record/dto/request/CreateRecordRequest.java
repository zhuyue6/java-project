package com.zhuyue.clockin.modules.record.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.zhuyue.clockin.common.validate.DateFormat;

@Data
public class CreateRecordRequest {
  private String remark;
  @DateFormat
  @NotNull(message = "打卡时间不能为空")
  private String clockInTime;
  @DateFormat
  private String clockOutTime;
  private long labelId;
}
