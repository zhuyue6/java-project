package com.zhuyue.clockin.modules.label.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RemoveLabelRequest {
  @NotNull(message = "标签ID不能为空")
  private long id;
}
