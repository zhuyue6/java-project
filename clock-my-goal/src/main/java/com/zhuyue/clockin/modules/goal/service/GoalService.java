package com.zhuyue.clockin.modules.goal.service;

import java.util.List;

import com.zhuyue.clockin.modules.goal.dto.request.*;
import com.zhuyue.clockin.modules.goal.dto.response.GoalResponse;

public interface GoalService {
  /**
   * 创建目标
   * @param userId 用户ID
   * @param dto 目标请求DTO
   * @return 目标ID
   */
  public long createGoal(Long userId, CreateGoalRequest dto);

  /**
   * 获取目标
   * @param userId 用户ID
   * @param dto 获取目标请求DTO
   * @return 目标响应DTO
   */
  public GoalResponse getGoal(long userId, GetGoalRequest dto);

  /**
   * 获取用户所有目标
   * @param userId 用户ID
   * @return 目标响应DTO列表
   */
  public List<GoalResponse> getGoals(long userId);

  /**
   * 删除目标
   * @param userId 用户ID
   * @param dto 删除目标请求DTO
   */
  public void removeGoal(long userId, RemoveGoalRequest dto);

  /**
   * 更新目标
   * @param userId 用户ID
   * @param dto 更新目标请求DTO
   */
  public void updateGoal(long userId, UpdateGoalRequest dto);

  /**
   * 目标打卡
   * @param dto
   */
  public void clockInGoal(long userId, ClockInGoalRequest dto);
}
