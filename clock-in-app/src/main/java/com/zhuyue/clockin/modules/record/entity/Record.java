package com.zhuyue.clockin.modules.record.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data 
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
  /** 打卡记录ID */
  private long id;
  /** 用户ID */
  private long userId;
  /** 备注 */
  private String remark;
  /** 打卡时间 */
  private String clockInTime;
  /** 打卡结束时间 */
  private String clockOutTime;
  /** 标签ID */
  private long labelId;
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
}
