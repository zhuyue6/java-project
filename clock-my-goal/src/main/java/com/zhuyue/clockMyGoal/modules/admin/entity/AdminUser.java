package com.zhuyue.clockMyGoal.modules.admin.entity;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {
  /**
   * 用户ID
   */
  private Long id;
  /**
   * 用户名
   */
  private String userName;
  /**
   * 密码
   */
  private String password;
  /**
   * 角色 1:超级管理员 2:管理员
   */
  private Integer role;
  /**
   * 状态 1:正常 2:禁用
   */
  private Integer status;
  /**
   * 创建时间
   */
  private LocalDateTime createTime;
  /**
   * 更新时间
   */
  private LocalDateTime updateTime;
}