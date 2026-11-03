package com.zhuyue.clockMyGoal.modules.goal.service.impl;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

import java.util.List;
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

    String expirationTimeString = TimeService.getDateTimeStringByDateTime(goal.getExpirationTime());
    String createTimeString = TimeService.getDateTimeStringByDateTime(goal.getCreateTime());
    String updateTimeString = TimeService.getDateTimeStringByDateTime(goal.getUpdateTime());
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
    goalResponse.setStartCheckTime(TimeService.getTimeStringByTime(goal.getStartCheckTime()));
    goalResponse.setEndCheckTime(TimeService.getTimeStringByTime(goal.getEndCheckTime()));
    goalResponse.setTargetClockInCycleCount(goal.getTargetClockInCycleCount());
    goalResponse.setClockInCycleCount(goal.getClockInCycleCount());
    goalResponse.setClockInCount(goal.getClockInCount());
    goalResponse.setTargetClockInCount(goal.getTargetClockInCount());
    goalResponse.setClockInCycle(goal.getClockInCycle());
    goalResponse.setCompletionRate(completionRate);
    goalResponse.setStatus(status);
    return goalResponse;
  }


  @Override
  public long createGoal(long userId, CreateGoalRequest dto) throws BusinessException {
    
    // 使用联合唯一索引查询是否存在相同目标名称，同一个用户只能创建一个相同目标名称的目标
    Goal existGoal = goalMapper.selectGoalByGoalName(userId, dto.getGoalName());
    if (existGoal != null) {
      throw new BusinessException(ExceptionCodeEnum.GoalNameAlreadyExists);
    }

    // 这里要计算当前时间与截止时间的差值，然后除以打卡周期，得到目标打卡次数
    int clockInCycle = dto.getClockInCycle();
    LocalDateTime expirationTime = dto.getExpirationTime();
    LocalDateTime currentTime = LocalDateTime.now();
    long targetClockInCount = TimeService.getTimeDifference(clockInCycle, currentTime, expirationTime);

    int targetClockInCycleCount = dto.getTargetClockInCycleCount() == null ? 1 : dto.getTargetClockInCycleCount();

    if (currentTime.isAfter(expirationTime)) {
      throw new BusinessException(ExceptionCodeEnum.GoalExpirationTimeInvalid);
    }

    ArrayList<LocalTime> checkTimeList = buildCheckTimeList(dto.getStartCheckTime(), dto.getEndCheckTime());

    Goal goal = Goal.builder()
        .goalName(dto.getGoalName())
        .expirationTime(expirationTime)
        .clockInCount(0)
        .clockInCycleCount(0)
        .targetClockInCycleCount(targetClockInCycleCount)
        .startCheckTime(checkTimeList.get(0))
        .endCheckTime(checkTimeList.get(1))
        .remark(dto.getRemark())
        .clockInCycle(clockInCycle)
        .labelId(dto.getLabelId())
        .build();

    goal.setTargetClockInCount(targetClockInCount);

    goalMapper.insertGoal(userId, goal);
    return goal.getId();
  }

  private ArrayList<LocalTime> buildCheckTimeList(LocalTime startCheckTime, LocalTime endCheckTime) {
    // 如果周期开始时间和结束时间为空，则直接按全时段处理
    ArrayList<LocalTime> checkTimeList = new ArrayList<>();
    if (startCheckTime == null || endCheckTime == null) {
      checkTimeList.add(TimeService.getTimeByTimeString("00:00:00"));
      checkTimeList.add(TimeService.getTimeByTimeString("23:59:59"));
    } else if (startCheckTime.isAfter(endCheckTime)) {
      throw new BusinessException(ExceptionCodeEnum.GoalCheckTimeStartTimeAfterEndTime);
    } else {
      checkTimeList.add(startCheckTime);
      checkTimeList.add(endCheckTime);
    }
    return checkTimeList;
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
    Goal lastGoal = requireGoal(userId, id);
    
    // 合并dto 与 goal 的非空属性
    Goal goal = mergeGoalWithDirty(lastGoal, dto);
    
    // 如果周期或截止时间发生变化，则目标打卡次数需要重新计算
    int clockInCycle = goal.getClockInCycle();
    LocalDateTime expirationTime = goal.getExpirationTime();
    long targetClockInCount = goal.getTargetClockInCount();
    if (clockInCycle != lastGoal.getClockInCycle() || !expirationTime.equals(lastGoal.getExpirationTime())) {
      LocalDateTime createTime = lastGoal.getCreateTime();
      targetClockInCount = TimeService.getTimeDifference(clockInCycle, createTime, expirationTime);
      goal.setTargetClockInCount(targetClockInCount);
      goal.setClockInCount(0);
    }

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
  private Goal mergeGoalWithDirty(Goal goal, UpdateGoalRequest dto) {
    Goal mergeGoal = new Goal();
    // 合并dto 与 goal 的非空属性
    mergeGoal.setGoalName(dto.getGoalName() != null ? dto.getGoalName() : goal.getGoalName());
    mergeGoal.setExpirationTime(dto.getExpirationTime() != null ? dto.getExpirationTime() : goal.getExpirationTime());
    mergeGoal.setRemark(dto.getRemark() != null ? dto.getRemark() : goal.getRemark());
    mergeGoal.setLabelId(dto.getLabelId() != null ? dto.getLabelId() : goal.getLabelId());
    mergeGoal.setClockInCycle(dto.getClockInCycle() != null ? dto.getClockInCycle() : goal.getClockInCycle());
    mergeGoal.setClockInCount(goal.getClockInCount());
    mergeGoal.setTargetClockInCount(goal.getTargetClockInCount());
    mergeGoal.setClockInTime(goal.getClockInTime());
    mergeGoal.setCreateTime(goal.getCreateTime());
    mergeGoal.setUpdateTime(goal.getUpdateTime());
    mergeGoal.setId(goal.getId());
    mergeGoal.setUserId(goal.getUserId());
    return mergeGoal;
  }

  /**
   * 打卡目标, 目标打卡分两步：
   * 1. 判断是否在打卡段内，周期内总打卡累积，同周期内可以重复打卡；
   * 2. 本周期已完成打卡，时间没到下周期打卡时间，不能打卡
   * @param userId
   * @param id
   * @throws BusinessException
   */
  @Override 
  public void clockInGoal(long userId, long id) throws BusinessException{
    Goal goal = requireGoal(userId, id);
    
    // 当前时间
    LocalDateTime currentTime = LocalDateTime.now();

    checkClockInTime(goal);

    int clockInCycleCount = goal.getClockInCycleCount();
    int targetClockInCycleCount = goal.getTargetClockInCycleCount();
    if (clockInCycleCount >= targetClockInCycleCount) {
      goal.setClockInCount(goal.getClockInCount() + 1);
      goal.setClockInCycleCount(0);
    } else {
      goal.setClockInCycleCount(clockInCycleCount + 1);
    }

    goalMapper.updateGoalClockInTime(userId, goal, currentTime);
  }

  /**
   * 判断是否在打卡周期内
   * @param clockInTime
   * @param currentTime
   * @return
   */
  private void checkClockInTime(Goal goal) {
    LocalDateTime currentDataTime = LocalDateTime.now();
    LocalTime currentTime = currentDataTime.toLocalTime();
    LocalTime startCheckTime = goal.getStartCheckTime();
    LocalTime endCheckTime = goal.getEndCheckTime();
    LocalDateTime clockInTime = goal.getClockInTime();
    int clockInCycle = goal.getClockInCycle();

    // 时间段检查：如果不在可打卡段内
    if (!(startCheckTime.isBefore(currentTime) && endCheckTime.isAfter(currentTime))) {
      throw new BusinessException(ExceptionCodeEnum.GoalNotInClockInTime);
    }
    
    // 没有打卡时间，则直接返回
    if (clockInTime == null) {
      return;
    }

    // 不同周期重复打卡检查：只检查周期是否已完成的打卡，如果已经打卡，则抛出异常
    boolean isSameCycle = false;
    if (clockInCycle == 1) {
      // 如果周期是天，对比当天是否打卡，并且时间是否晚于上次打卡时间
      boolean isSameDay = clockInTime.toLocalDate().equals(currentDataTime.toLocalDate());
      isSameCycle =  isSameDay || clockInTime.isAfter(currentDataTime);
    } else if (clockInCycle == 2) {
      // 如果周期是周，对比本周是否打卡，并且时间是否晚于上次打卡时间
      boolean isSameWeek = TimeService.isSameWeek(clockInTime, currentDataTime);
      isSameCycle = isSameWeek || clockInTime.isAfter(currentDataTime);
    } else if (clockInCycle == 3) {
      // 如果周期是月，对比本月是否打卡，并且时间是否晚于上次打卡时间
      boolean isSameMonth = clockInTime.getMonth().equals(currentDataTime.getMonth());
      isSameCycle = isSameMonth || clockInTime.isAfter(currentDataTime);
    }

    // 如果是同一周期，并且周期计数大于等于目标周期计数，则抛出异常
    int clockInCycleCount = goal.getClockInCycleCount();
    int targetClockInCycleCount = goal.getTargetClockInCycleCount();
    if (isSameCycle && clockInCycleCount >= targetClockInCycleCount) {
      throw new BusinessException(ExceptionCodeEnum.GoalAlreadyClockedIn);
    }
  }
}
