package com.zhuyue.clockin.modules.record.dto.response;

import lombok.*;

@Data
@Builder
public class RecordResponse {
  /** 打卡记录ID */
  private long id;
  /** 打卡时间 */
  private String clockInTime;
  /** 打卡结束时间 */
  private String clockOutTime;
  /** 备注 */
  private String remark;
  /** 标签名称 */
  private String labelName;
  /** 标签类型 */
  private String labelType;
}
