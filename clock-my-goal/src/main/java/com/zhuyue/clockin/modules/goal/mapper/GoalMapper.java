package com.zhuyue.clockin.modules.goal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhuyue.clockin.modules.goal.entity.Goal;

import java.util.List;

@Mapper
public interface GoalMapper {
  // insert 返回影响行数；自增 id 回写到 goal.id
  void insertGoal(@Param("userId") long userId, @Param("goal") Goal goal);
  void updateGoal(@Param("userId") long userId, @Param("goal") Goal goal);
  void deleteGoal(@Param("userId") long userId, @Param("id") long id);
  Goal selectGoalById(@Param("userId") long userId, @Param("id") long id);
  List<Goal> selectGoalByUserId(@Param("userId") long userId);
}
