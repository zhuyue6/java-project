package com.zhuyue.clockin.modules.goal.dto.response;

import lombok.*;

@Data
@Builder
public class GoalResponse {
  /** 目标ID */
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
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
  /** 已打卡次数 */
  private int clockInCount;
  /** 目标打卡次数 */
  private int targetClockInCount;
  /** 完成率 */
  private double completionRate;
  /** 打卡周期(天/周/月) */
  private int clockInCycle;
}
