package com.zhuyue.clockMyGoal.modules.admin.dto.response;

import lombok.Data;

@Data
public class AdminUserResponse {
  /**
   * 用户ID
   */
  private Long id;
  /**
   * 用户名
   */
  private String userName;
  /**
   * 角色
   */
  private String role;
  /**
   * 状态 1:正常 2:禁用
   */
  private Integer status;
  /**
   * 创建时间
   */
  private String createTime;
  /**
   * 更新时间
   */
  private String updateTime;
}
