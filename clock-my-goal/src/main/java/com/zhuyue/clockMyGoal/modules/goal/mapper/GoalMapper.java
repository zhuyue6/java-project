package com.zhuyue.clockMyGoal.modules.goal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhuyue.clockMyGoal.modules.goal.entity.*;

import java.util.List;
import java.time.LocalDateTime;

@Mapper
public interface GoalMapper {
  // insert 返回影响行数；自增 id 回写到 goal.id
  void insertGoal(@Param("userId") long userId, @Param("goal") Goal goal);
  void updateGoal(@Param("userId") long userId, @Param("goal") Goal goal);
  Goal selectGoalByGoalName(@Param("userId") long userId, @Param("goalName") String goalName);
  void updateGoalNonNull(@Param("userId") long userId, @Param("goal") Goal goal);
  void updateGoalClockInTime(@Param("userId") long userId, @Param("goal") Goal goal, @Param("clockInTime") LocalDateTime clockInTime);
  void deleteGoal(@Param("userId") long userId, @Param("id") long id);
  Goal selectGoalById(@Param("userId") long userId, @Param("id") long id);
  List<Goal> selectGoalByUserId(@Param("userId") long userId);

  void insertGoalClockIn(@Param("userId") long userId, @Param("goalId") long goalId, @Param("clockInTime") LocalDateTime clockInTime);
  List<GoalClockIn> selectGoalClockInByGoalId(@Param("userId") long userId, @Param("goalId") long goalId, @Param("cycleStartTime") LocalDateTime cycleStartTime, @Param("cycleEndTime") LocalDateTime cycleEndTime);
}
