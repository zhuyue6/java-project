package com.zhuyue.clockMyGoal.modules.admin.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlatformUserResponse {
  /** 用户ID */
  private long id;
  /** 用户名 */
  private String userName;
  /** 性别 */
  private Integer sex;
  /** 年龄 */
  private int age;
  /** 头像 */
  private String avatarUrl;
  /** 状态 */
  private Integer status;
  /** 创建时间 */
  private String createTime;
  /** 进行中 */
  private int ongoing;
  /** 已完成 */
  private int completed;
  /** 已过期 */
  private int expired;
  /** 标签数量 */
  private int labelCount;
  /** 计划列表 */
  private List<PlatformUserPlanResponse> plans;
}
