package com.zhuyue.clockin.modules.punch.dto.response;

import lombok.Data;

@Data
public class CreateRecordResponse {
  /** 打卡记录ID */
  private Long id;
  /** 打卡时间 */
  private String clockInTime;
  /** 打卡结束时间 */
  private String clockOutTime;
  /** 备注 */
  private String remark;
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
}
