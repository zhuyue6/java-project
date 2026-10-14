package com.zhuyue.clockin.modules.goal.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
  /** 目标ID */
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
  /** 已打卡次数 */
  private int clockInCount;
  /** 目标打卡次数 */
  private int targetClockInCount;
  /** 打卡周期(天/周/月) */
  private int clockInCycle;
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
}
