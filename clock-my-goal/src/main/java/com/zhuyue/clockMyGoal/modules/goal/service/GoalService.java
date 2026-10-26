package com.zhuyue.clockMyGoal.modules.goal.service;

import java.util.List;

import com.zhuyue.clockMyGoal.modules.goal.dto.request.*;
import com.zhuyue.clockMyGoal.modules.goal.dto.response.GoalResponse;

public interface GoalService {
  /**
   * 创建目标
   * @param userId 用户ID
   * @param dto 目标请求DTO
   * @return 目标ID
   */
  public long createGoal(long userId, CreateGoalRequest dto);

  /**
   * 获取目标
   * @param userId 用户ID
   * @param id 目标ID
   * @return 目标响应DTO
   */
  public GoalResponse getGoal(long userId, long id);

  /**
   * 获取用户所有目标
   * @param userId 用户ID
   * @return 目标响应DTO列表
   */
  public List<GoalResponse> getGoals(long userId);

  /**
   * 删除目标
   * @param userId 用户ID
   * @param id 目标ID
   */
  public void removeGoal(long userId, long id);

  /**
   * 更新目标
   * @param userId 用户ID
   * @param id 目标ID
   * @param dto 更新目标请求DTO
   */
  public void updateGoal(long userId, long id, UpdateGoalRequest dto);

  /**
   * 目标打卡
   * @param userId 用户ID
   * @param id 目标ID
   */
  public void clockInGoal(long userId, long id);
}
