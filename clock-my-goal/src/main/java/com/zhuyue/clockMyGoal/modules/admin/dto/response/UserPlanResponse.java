package com.zhuyue.clockMyGoal.modules.admin.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPlanResponse {
  /** 计划ID */
  private long id;
  /** 计划名称 */
  private String name;
  /** 1 日，2 周，3 月 */
  private int cycle;
  /** 已打卡次数 */ 
  private int clockInCount;
  /** 目标打卡次数 */
  private long targetClockInCount;
  /** 1 进行中，2 已完成，3 已过期 */
  private int status;
  /** 到期时间 */
  private String expireTime;
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
}
