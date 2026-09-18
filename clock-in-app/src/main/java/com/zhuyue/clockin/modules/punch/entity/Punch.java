package com.zhuyue.clockin.modules.punch.entity;

import lombok.Data;
import lombok.Builder;

@Data 
@Builder
public class Punch {
  /** 打卡记录ID */
  private Long id;
  /** 用户ID */
  private Long userId;
  /** 备注 */
  private String remark;
  /** 打卡时间 */
  private String clockInTime;
  /** 打卡结束时间 */
  private String clockOutTime;
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
}
