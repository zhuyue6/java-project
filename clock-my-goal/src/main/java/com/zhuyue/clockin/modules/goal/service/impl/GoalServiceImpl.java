package com.zhuyue.clockin.modules.goal.service.impl;

import org.springframework.stereotype.Service;
import com.zhuyue.clockin.modules.label.service.LabelService;
import com.zhuyue.clockin.modules.goal.dto.request.*;
import com.zhuyue.clockin.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockin.modules.goal.entity.Goal;
import com.zhuyue.clockin.modules.goal.mapper.GoalMapper;
import com.zhuyue.clockin.modules.goal.service.GoalService;
import com.zhuyue.clockin.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockin.common.exception.BusinessException;
import com.zhuyue.clockin.common.constants.*;
import java.math.BigDecimal;

import java.util.List;
import jakarta.annotation.Resource;
import java.util.ArrayList;

@Service("GoalService")
public class GoalServiceImpl implements GoalService {
  @Resource
  private GoalMapper goalMapper;
  @Resource
  private LabelService labelService;

  @Override
  public long createGoal(Long userId, CreateGoalRequest dto) {
    Goal goal = Goal.builder()
        .clockInTime(dto.getClockInTime())
        .clockOutTime(dto.getClockOutTime())
        .remark(dto.getRemark())
        .labelId(dto.getLabelId())
        .build();

    goalMapper.insertGoal(userId, goal);

    return goal.getId();
  }

  @Override
  public GoalResponse getGoal(long userId, GetGoalRequest dto) {
    Goal goal = goalMapper.selectGoalById(userId, dto.getId());
    LabelResponse matchLabel = labelService.getLabelById(goal.getLabelId());
    return GoalResponse.builder()
      .id(goal.getId())
      .clockInTime(goal.getClockInTime())
      .clockOutTime(goal.getClockOutTime())
      .remark(goal.getRemark())
      .labelName(matchLabel != null ? matchLabel.getLabelName() : null)
      .labelType(matchLabel != null ? matchLabel.getLabelType() : null)
      .build();
  }

  @Override
  public List<GoalResponse> getGoals(long userId) {
    List<Goal> goalList = goalMapper.selectGoalByUserId(userId);
    List<LabelResponse> labelList = labelService.getLabelListByUserId(userId);
    List<GoalResponse> responseList = new ArrayList<>();

    for (Goal goal : goalList) {
      LabelResponse matchLabel = labelList.stream().filter(r -> r.getId() == goal.getLabelId()).findFirst().orElse(null);

      double completionRate = 0.0;
      if (goal.getTargetClockInCount() > 0) {
        completionRate = (double) goal.getClockInCount() / goal.getTargetClockInCount();
        BigDecimal completionRateBigDecimal = new BigDecimal(completionRate);
        completionRate = completionRateBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
      }
      GoalResponse response = GoalResponse.builder()
        .id(goal.getId())
        .clockInTime(goal.getClockInTime())
        .clockOutTime(goal.getClockOutTime())
        .remark(goal.getRemark())
        .labelName(matchLabel != null ? matchLabel.getLabelName() : null)
        .labelType(matchLabel != null ? matchLabel.getLabelType() : null)
        .clockInCount(goal.getClockInCount())
        .targetClockInCount(goal.getTargetClockInCount())
        .clockInCycle(goal.getClockInCycle())
        .completionRate(completionRate)
        .createTime(goal.getCreateTime())
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
    Goal goal = Goal.builder()
      .id(dto.getId())
      .clockInTime(dto.getClockInTime())
      .clockOutTime(dto.getClockOutTime())
      .remark(dto.getRemark())
      .labelId(dto.getLabelId())
      .targetClockInCount(dto.getTargetClockInCount())
      .clockInCycle(dto.getClockInCycle())
      .build();
    goalMapper.updateGoal(userId, goal);
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
