package com.zhuyue.clockMyGoal.modules.goal.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalClockIn {
  /** 打卡记录ID */
  private long id;
  /** 关联打卡目标ID */
  private long goalId;
  /** 打卡时间 */
  private LocalDateTime clockInTime;
  /** 创建时间 */
  private LocalDateTime createTime;
  /** 更新时间 */
  private LocalDateTime updateTime;
}
