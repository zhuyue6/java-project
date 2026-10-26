package com.zhuyue.clockMyGoal.modules.user.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Data
public class PageRequest {
  @Min(value = 1, message = "页码不能小于1")
  @Max(value = 1000, message = "页码不能大于1000")
  @NotNull(message = "页大小不能为空")
  private Integer pageSize;
  
  @NotNull(message = "页大小不能为空")
  @Min(value = 1, message = "页大小不能小于1")
  private Integer pageNum;
}
