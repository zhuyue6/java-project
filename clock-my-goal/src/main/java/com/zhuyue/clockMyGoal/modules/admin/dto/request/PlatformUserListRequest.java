package com.zhuyue.clockMyGoal.modules.admin.dto.request;

import lombok.Data;

@Data
public class PlatformUserListRequest {
  /** 关键词 */
  private String keyword;
  /** 状态 */
  private int status;
  /** 页码 */
  private Integer pageNum;
  /** 每页条数 */
  private Integer pageSize;
}
