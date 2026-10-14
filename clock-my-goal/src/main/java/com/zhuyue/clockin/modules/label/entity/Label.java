package com.zhuyue.clockin.modules.label.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Label {
  /** 打卡记录ID */
  private long id;
  /** 关联用户ID */
  private long userId;
  /** 用户ID */
  private String labelType;
  /** 标签名称 */
  private String labelName;
  /** 创建时间 */
  private String createTime;
  /** 更新时间 */
  private String updateTime;
}
