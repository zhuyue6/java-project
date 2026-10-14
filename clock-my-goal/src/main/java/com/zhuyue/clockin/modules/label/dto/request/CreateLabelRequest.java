package com.zhuyue.clockin.modules.label.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateLabelRequest {
  @NotNull(message = "标签名称不能为空")
  private String labelName;
  @NotNull(message = "标签类型不能为空")
  private String labelType;
}