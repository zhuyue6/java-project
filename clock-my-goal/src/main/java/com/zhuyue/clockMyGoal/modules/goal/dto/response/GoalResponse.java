package com.zhuyue.clockMyGoal.modules.goal.dto.response;

import lombok.*;

@Data
@Builder
public class GoalResponse {
  /** 目标ID */
  private long id;
  /** 打卡到期时间 */
  private String expirationTime;
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
  private long targetClockInCount;
  /** 完成率 */
  private double completionRate;
  /** 打卡周期(1天/2周/3月) */
  private int clockInCycle;
}
