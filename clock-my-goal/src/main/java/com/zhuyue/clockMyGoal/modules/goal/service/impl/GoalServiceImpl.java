package com.zhuyue.clockMyGoal.modules.goal.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import jakarta.annotation.Resource;

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
import com.zhuyue.clockMyGoal.common.util.TimeService;

@Service("GoalService")
public class GoalServiceImpl implements GoalService {
  @Resource
  private GoalMapper goalMapper;
  @Resource
  private LabelService labelService;

  private GoalResponse convertToGoalResponse(Goal goal, LabelResponse labelResponse) {
    if (goal == null) {
      return null;
    }

    GoalResponse goalResponse = new GoalResponse();

    String expirationTimeString = TimeService.getTimeStringByTime(goal.getExpirationTime());
    String createTimeString = TimeService.getTimeStringByTime(goal.getCreateTime());
    String updateTimeString = TimeService.getTimeStringByTime(goal.getUpdateTime());
    double completionRate = calculateCompletionRate(goal.getClockInCount(), goal.getTargetClockInCount());
    int status = getGoalStatus(goal);
    goalResponse.setId(goal.getId());
    goalResponse.setGoalName(goal.getGoalName());
    goalResponse.setExpirationTime(expirationTimeString);
    goalResponse.setRemark(goal.getRemark());
    goalResponse.setLabelName(labelResponse == null ? null : labelResponse.getLabelName());
    goalResponse.setLabelType(labelResponse == null ? null : labelResponse.getLabelType());
    goalResponse.setCreateTime(createTimeString);
    goalResponse.setUpdateTime(updateTimeString);
    goalResponse.setClockInCount(goal.getClockInCount());
    goalResponse.setTargetClockInCount(goal.getTargetClockInCount());
    goalResponse.setClockInCycle(goal.getClockInCycle());
    goalResponse.setCompletionRate(completionRate);
    goalResponse.setStatus(status);
    return goalResponse;
  }


  @Override
  public long createGoal(long userId, CreateGoalRequest dto) throws BusinessException {
    // 这里要计算当前时间与截止时间的差值，然后除以打卡周期，得到目标打卡次数
    int clockInCycle = dto.getClockInCycle();
    LocalDateTime expirationTime = dto.getExpirationTime();
    LocalDateTime currentTime = LocalDateTime.now();
    long targetClockInCount = TimeService.getTimeDifference(clockInCycle, currentTime, expirationTime);

    if (currentTime.isAfter(expirationTime)) {
      throw new BusinessException(ExceptionCodeEnum.GoalExpirationTimeInvalid);
    }

    Goal goal = Goal.builder()
        .goalName(dto.getGoalName())
        .expirationTime(expirationTime)
        .clockInCount(0)
        .remark(dto.getRemark())
        .clockInCycle(clockInCycle)
        .labelId(dto.getLabelId())
        .build();

    goal.setTargetClockInCount(targetClockInCount);

    goalMapper.insertGoal(userId, goal);
    return goal.getId();
  }

  @Override
  public GoalResponse getGoal(long userId, long id) {
    Goal goal = goalMapper.selectGoalById(userId, id);
    if (goal == null) {
      return null;
    }
    LabelResponse matchLabel = labelService.getLabelById(goal.getLabelId());
    GoalResponse goalResponse = convertToGoalResponse(goal, matchLabel);
    return goalResponse;
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

      GoalResponse goalResponse = convertToGoalResponse(goal, matchLabel);
      responseList.add(goalResponse);
    }

    return responseList;
  }

  @Override
  public void removeGoal(long userId, long id) {
    requireGoal(userId, id);
    goalMapper.deleteGoal(userId, id);
  }

  @Override
  public void updateGoal(long userId, long id, UpdateGoalRequest dto) {
    Goal lastGoal = requireGoal(userId, id);;
    int clockInCycle = Objects.requireNonNullElse(dto.getClockInCycle(), lastGoal.getClockInCycle());
    LocalDateTime expirationTime = Objects.requireNonNullElse(dto.getExpirationTime(), lastGoal.getExpirationTime());
    
    Goal goal = Goal.builder()
      .id(id)
      .goalName(dto.getGoalName())
      .expirationTime(expirationTime)
      .remark(dto.getRemark())
      .labelId(dto.getLabelId())
      .clockInCycle(clockInCycle)
      .build();

    // 如果周期或截止时间发生变化，则目标打卡次数需要重新计算
    if (clockInCycle != lastGoal.getClockInCycle() && !expirationTime.equals(lastGoal.getExpirationTime())) {
      LocalDateTime createTime = lastGoal.getCreateTime();
      long targetClockInCount = TimeService.getTimeDifference(clockInCycle, createTime, expirationTime);
      goal.setTargetClockInCount(targetClockInCount);
    }

    Goal mergeGoal = mergeGoalWithDirty(lastGoal, goal);

    goalMapper.updateGoalNonNull(userId, mergeGoal);
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

  private Goal requireGoal(long userId, long id) {
    Goal goal = goalMapper.selectGoalById(userId, id);
    if (goal == null) {
      throw new BusinessException(ExceptionCodeEnum.GoalNotFound);
    }
    return goal;
  }

  /**
   * 合并目标，脏数据处理
   * @param goal
   * @param targetGoal
   * @return
   */
  private Goal mergeGoalWithDirty(Goal goal, Goal targetGoal) {
    Goal mergeGoal = new Goal();
    mergeGoal.setGoalName(Objects.requireNonNullElse(targetGoal.getGoalName(), goal.getGoalName()));
    mergeGoal.setExpirationTime(Objects.requireNonNullElse(targetGoal.getExpirationTime(), goal.getExpirationTime()));
    mergeGoal.setRemark(Objects.requireNonNullElse(targetGoal.getRemark(), goal.getRemark()));
    mergeGoal.setLabelId(Objects.requireNonNullElse(targetGoal.getLabelId(), goal.getLabelId()));
    mergeGoal.setClockInCount(Objects.requireNonNullElse(targetGoal.getClockInCount(), goal.getClockInCount()));
    mergeGoal.setClockInCycle(Objects.requireNonNullElse(targetGoal.getClockInCycle(), goal.getClockInCycle()));
    mergeGoal.setTargetClockInCount(Objects.requireNonNullElse(targetGoal.getTargetClockInCount(), goal.getTargetClockInCount()));
    mergeGoal.setClockInTime(Objects.requireNonNullElse(targetGoal.getClockInTime(), goal.getClockInTime()));
    mergeGoal.setCreateTime(Objects.requireNonNullElse(targetGoal.getCreateTime(), goal.getCreateTime()));
    mergeGoal.setUpdateTime(Objects.requireNonNullElse(targetGoal.getUpdateTime(), goal.getUpdateTime()));
    return mergeGoal;
  }

  @Override 
  public void clockInGoal(long userId, long id) throws BusinessException{
    Goal goal = requireGoal(userId, id);
    // 当前时间
    LocalDateTime currentTime = LocalDateTime.now();
    
    // 上次打卡时间
    LocalDateTime clockInTime = goal.getClockInTime();
    
    if (isInClockInTime(clockInTime, currentTime, goal.getClockInCycle())) {
      throw new BusinessException(ExceptionCodeEnum.GoalAlreadyClockedIn);
    }

    goal.setClockInCount(goal.getClockInCount() + 1);

    goalMapper.updateGoalClockInTime(userId, goal, currentTime);
  }

  /**
   * 判断是否在打卡周期内
   * @param clockInTime
   * @param currentTime
   * @return
   */
  private boolean isInClockInTime(LocalDateTime clockInTime, LocalDateTime currentTime, int clockInCycle) {
    if (clockInTime == null) {
      return false;
    }

    if (clockInCycle == 1) {
      // 如果周期是天，对比当天是否打卡，并且时间是否晚于上次打卡时间
      boolean isSameDay = clockInTime.toLocalDate().equals(currentTime.toLocalDate());
      return isSameDay || clockInTime.isAfter(currentTime);
    } else if (clockInCycle == 2) {
      // 如果周期是周，对比本周是否打卡，并且时间是否晚于上次打卡时间
      boolean isSameWeek = TimeService.isSameWeek(clockInTime, currentTime);
      return isSameWeek || clockInTime.isAfter(currentTime);
    } else if (clockInCycle == 3) {
      // 如果周期是月，对比本月是否打卡，并且时间是否晚于上次打卡时间
      boolean isSameMonth = clockInTime.getMonth().equals(currentTime.getMonth());
      return isSameMonth || clockInTime.isAfter(currentTime);
    }
    return false;
  }
}
