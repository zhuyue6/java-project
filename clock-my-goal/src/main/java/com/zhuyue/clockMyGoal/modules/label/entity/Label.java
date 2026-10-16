package com.zhuyue.clockMyGoal.modules.label.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
  private LocalDateTime createTime;
  /** 更新时间 */
  private LocalDateTime updateTime;
}
