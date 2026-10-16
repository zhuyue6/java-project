package com.zhuyue.clockMyGoal.modules.goal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhuyue.clockMyGoal.common.interceptor.LoginUser;
import com.zhuyue.clockMyGoal.modules.goal.dto.request.*;
import com.zhuyue.clockMyGoal.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockMyGoal.modules.goal.service.GoalService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.web.bind.annotation.RequestAttribute;


@RestController
@RequestMapping("/goal")
public class GoalController {

  @Resource(name = "GoalService")
  private GoalService goalService;

  @PostMapping("/createGoal")
  public long createGoal(@Valid @RequestBody CreateGoalRequest createGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.createGoal(userId, createGoalRequest);
  }

  @PostMapping("/updateGoal")
  public void updateGoal(@Valid @RequestBody UpdateGoalRequest updateGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.updateGoal(userId, updateGoalRequest);
  }

  @GetMapping("/getGoal")
  public GoalResponse getGoal(@Valid @ModelAttribute GetGoalRequest getGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.getGoal(userId, getGoalRequest);
  }

  @GetMapping("/getGoals")
  public List<GoalResponse> getGoals(@RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.getGoals(userId);
  }

  @PostMapping("/removeGoal")
  public void removeGoal(@Valid @RequestBody RemoveGoalRequest removeGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.removeGoal(userId, removeGoalRequest);
  }

  @PostMapping("/clockInGoal")
  public void clockInGoal(@Valid @RequestBody ClockInGoalRequest clockInGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.clockInGoal(userId, clockInGoalRequest);
  }
}
