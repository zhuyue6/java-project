package com.zhuyue.clockin.modules.goal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.annotation.Resource;
import com.zhuyue.clockin.common.interceptor.LoginUser;
import com.zhuyue.clockin.modules.goal.dto.request.*;
import com.zhuyue.clockin.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockin.modules.goal.service.GoalService;

import java.util.List;
import org.springframework.web.bind.annotation.RequestAttribute;


@RestController
@RequestMapping("/goal")
public class GoalController {

  @Resource(name = "GoalService")
  private GoalService goalService;

  @PostMapping("/createGoal")
  public long createGoal(@RequestBody CreateGoalRequest createGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.createGoal(userId, createGoalRequest);
  }

  @PostMapping("/updateGoal")
  public void updateGoal(@RequestBody UpdateGoalRequest updateGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.updateGoal(userId, updateGoalRequest);
  }

  @GetMapping("/getGoal")
  public GoalResponse getGoal(@RequestBody GetGoalRequest getGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.getGoal(userId, getGoalRequest);
  }

  @GetMapping("/getGoals")
  public List<GoalResponse> getGoals(@RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.getGoals(userId);
  }

  @PostMapping("/removeGoal")
  public void removeGoal(@RequestBody RemoveGoalRequest removeGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.removeGoal(userId, removeGoalRequest);
  }

  @PostMapping("/clockIn")
  public void clockInGoal(@RequestBody RemoveGoalRequest removeGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    goalService.clockInGoal(userId, removeGoalRequest);
  }
}
