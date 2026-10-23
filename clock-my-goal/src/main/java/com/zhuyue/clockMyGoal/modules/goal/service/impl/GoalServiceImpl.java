package com.zhuyue.clockMyGoal.modules.goal.service.impl;

import org.springframework.stereotype.Service;

import com.zhuyue.clockMyGoal.common.constants.*;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.modules.goal.dto.request.*;
import com.zhuyue.clockMyGoal.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockMyGoal.modules.goal.entity.Goal;
import com.zhuyue.clockMyGoal.modules.goal.mapper.GoalMapper;
import com.zhuyue.clockMyGoal.modules.goal.service.GoalService;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockMyGoal.modules.label.service.LabelService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import com.zhuyue.clockMyGoal.common.util.TimeService;

@Service("GoalService")
public class GoalServiceImpl implements GoalService {
  @Resource
  private GoalMapper goalMapper;
  @Resource
  private LabelService labelService;

  @Override
  public long createGoal(long userId, CreateGoalRequest dto) {
    Goal goal = Goal.builder()
        .expirationTime(dto.getExpirationTime())
        .remark(dto.getRemark())
        .clockInCycle(dto.getClockInCycle())
        .labelId(dto.getLabelId())
        .build();

    // 这里要计算当前时间与截止时间的差值，然后除以打卡周期，得到目标打卡次数
    int clockInCycle = dto.getClockInCycle();
    LocalDateTime expirationTime = dto.getExpirationTime();
    LocalDateTime currentTime = LocalDateTime.now();
    long targetClockInCount = TimeService.getTimeDifference(clockInCycle, currentTime, expirationTime);
    goal.setTargetClockInCount(targetClockInCount);

    goalMapper.insertGoal(userId, goal);
    return goal.getId();
  }

  @Override
  public GoalResponse getGoal(long userId, GetGoalRequest dto) {
    Goal goal = goalMapper.selectGoalById(userId, dto.getId());
    LabelResponse matchLabel = labelService.getLabelById(goal.getLabelId());
    String expirationTimeString = TimeService.getTimeStringByTime(goal.getExpirationTime());
    String createTimeString = TimeService.getTimeStringByTime(goal.getCreateTime());
    String updateTimeString = TimeService.getTimeStringByTime(goal.getUpdateTime());
    int status = getGoalStatus(goal);

    double completionRate = calculateCompletionRate(goal.getClockInCount(), goal.getTargetClockInCount());
    return GoalResponse.builder()
      .id(goal.getId())
      .expirationTime(expirationTimeString)
      .remark(goal.getRemark())
      .labelName(matchLabel != null ? matchLabel.getLabelName() : null)
      .labelType(matchLabel != null ? matchLabel.getLabelType() : null)
      .createTime(createTimeString)
      .updateTime(updateTimeString)
      .clockInCycle(goal.getClockInCycle())
      .clockInCount(goal.getClockInCount())
      .targetClockInCount(goal.getTargetClockInCount())
      .completionRate(completionRate)
      .status(status)
      .build();
  }

  @Override
  public List<GoalResponse> getGoals(long userId) {
    List<Goal> goalList = goalMapper.selectGoalByUserId(userId);
    List<LabelResponse> labelList = labelService.getLabelListByUserId(userId);
    List<GoalResponse> responseList = new ArrayList<>();
    if (goalList == null) {
      return responseList;
    }

    for (Goal goal : goalList) {
      Long labelId = goal.getLabelId();
      LabelResponse matchLabel = labelId == null ? null
          : labelList.stream().filter(r -> r.getId() == labelId).findFirst().orElse(null);

      double completionRate = calculateCompletionRate(goal.getClockInCount(), goal.getTargetClockInCount());
      String expirationTimeString = TimeService.getTimeStringByTime(goal.getExpirationTime());
      String createTimeString = TimeService.getTimeStringByTime(goal.getCreateTime());
      int status = getGoalStatus(goal);
      GoalResponse response = GoalResponse.builder()
        .id(goal.getId())
        .expirationTime(expirationTimeString)
        .remark(goal.getRemark())
        .labelName(matchLabel != null ? matchLabel.getLabelName() : null)
        .labelType(matchLabel != null ? matchLabel.getLabelType() : null)
        .clockInCount(goal.getClockInCount())
        .targetClockInCount(goal.getTargetClockInCount())
        .clockInCycle(goal.getClockInCycle())
        .completionRate(completionRate)
        .createTime(createTimeString)
        .status(status)
        .build();
        responseList.add(response);
    }

    return responseList;
  }

  @Override
  public void removeGoal(long userId, RemoveGoalRequest dto) {
    goalMapper.deleteGoal(userId, dto.getId());
  }

  @Override
  public void updateGoal(long userId, UpdateGoalRequest dto) {

    Goal lastGoal = goalMapper.selectGoalById(userId, dto.getId());
    int clockInCycle = dto.getClockInCycle();
    LocalDateTime expirationTime = dto.getExpirationTime();
    LocalDateTime createTime = lastGoal.getCreateTime();

    Goal goal = Goal.builder()
      .id(dto.getId())
      .expirationTime(expirationTime)
      .remark(dto.getRemark())
      .labelId(dto.getLabelId())
      .clockInCycle(clockInCycle)
      .build();

    long targetClockInCount = TimeService.getTimeDifference(clockInCycle, createTime, expirationTime);
    goal.setTargetClockInCount(targetClockInCount);

    goalMapper.updateGoal(userId, goal);
  }
  
  /**
   * 获取目标状态，实时计算
   * @param goal
   * @return
   */
  public int getGoalStatus(Goal goal) {
    int clockInCount =goal.getClockInCount();
    long targetClockInCount = goal.getTargetClockInCount();
    
    if (clockInCount >= targetClockInCount) {
      return 2;
    }

    LocalDateTime expirationTime = goal.getExpirationTime();
    LocalDateTime currentTime = LocalDateTime.now();
    if (currentTime.isAfter(expirationTime)) {
      return 3;
    }

    return 1;
  }

  public double calculateCompletionRate(long clockInCount, long targetClockInCount) {
    double completionRate = 0.0;
    if (targetClockInCount > 0) {
      completionRate = (double) clockInCount / targetClockInCount;
      BigDecimal completionRateBigDecimal = new BigDecimal(completionRate);
      completionRate = completionRateBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    return completionRate;
  }

  @Override 
  public void clockInGoal(long userId, ClockInGoalRequest dto) {
    Goal goal = goalMapper.selectGoalById(userId, dto.getId());
    if (goal == null) {
      throw new BusinessException(ExceptionCodeEnum.GoalNotFound.getCode(), ExceptionCodeEnum.GoalNotFound.getMessage());
    }
    goal.setClockInCount(goal.getClockInCount() + 1);

    goalMapper.updateGoal(userId, goal);
  }
}
