package com.zhuyue.clockin.modules.label.dto.response;

import lombok.*;

@Data
@Builder
public class LabelResponse {
  /** 打卡记录ID */
  private long id;
  /** 标签名称 */
  private String labelName;
  /** 标签类型 */
  private String labelType;
}
