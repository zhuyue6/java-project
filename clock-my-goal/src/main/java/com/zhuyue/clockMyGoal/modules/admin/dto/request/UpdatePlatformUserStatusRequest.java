package com.zhuyue.clockMyGoal.modules.admin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePlatformUserStatusRequest {
  /** 0 停用，1 正常 */
  @NotNull(message = "请选择账号状态")
  @Min(value = 0, message = "账号状态不正确")
  @Max(value = 1, message = "账号状态不正确")
  private int status;
}
