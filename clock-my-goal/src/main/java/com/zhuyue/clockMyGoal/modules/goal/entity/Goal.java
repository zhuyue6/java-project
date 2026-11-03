package com.zhuyue.clockMyGoal.modules.goal.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
  /** 目标ID */
  private long id;
  /** 目标名称 */
  private String goalName;
  /** 用户ID */
  private long userId;
  /** 备注 */
  private String remark;
  /** 打卡到期时间 */
  private LocalDateTime expirationTime;
  /** 标签ID */
  private Long labelId;
  /** 目标打卡时间周期总计数 */
  private int targetClockInCycleCount;
  /** 已打卡时间周期计数 */
  private int clockInCycleCount;
  /** 可打卡段开始时间 */
  private LocalTime startCheckTime;
  /** 可打卡段结束时间 */
  private LocalTime endCheckTime;
  /** 已打卡次数 */
  private int clockInCount;
  /** 目标打卡次数 */
  private long targetClockInCount;
  /** 打卡周期(天/周/月) */
  private int clockInCycle;
  /** 上次打卡时间 */
  private LocalDateTime clockInTime;
  /** 创建时间 */
  private LocalDateTime createTime;
  /** 更新时间 */
  private LocalDateTime updateTime;
}
