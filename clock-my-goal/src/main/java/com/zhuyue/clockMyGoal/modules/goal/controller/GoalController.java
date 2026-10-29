package com.zhuyue.clockMyGoal.modules.goal.controller;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.zhuyue.clockMyGoal.common.interceptor.LoginUser;
import com.zhuyue.clockMyGoal.modules.goal.dto.request.*;
import com.zhuyue.clockMyGoal.modules.goal.dto.response.GoalResponse;
import com.zhuyue.clockMyGoal.modules.goal.service.GoalService;


@RestController
@RequestMapping("/goals")
@Validated 
public class GoalController {

  @Resource(name = "GoalService")
  private GoalService goalService;

  @PostMapping
  public long createGoal(@Valid @RequestBody CreateGoalRequest createGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.createGoal(userId, createGoalRequest);
  }

  @PatchMapping("/{id}")
  public void updateGoal(@PathVariable("id") long id, @Valid @RequestBody UpdateGoalRequest updateGoalRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.updateGoal(userId, id, updateGoalRequest);
  }

  @GetMapping("/{id}")
  public GoalResponse getGoal(@PathVariable("id") long id, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.getGoal(userId, id);
  }

  @GetMapping
  public List<GoalResponse> getGoals(@RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return goalService.getGoals(userId);
  }

  @DeleteMapping("/{id}")
  public void removeGoal(@PathVariable("id") long id, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.removeGoal(userId, id);
  }

  @PostMapping("/{id}/clockIn")
  public void clockInGoal(@PathVariable("id") long id, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    goalService.clockInGoal(userId, id);
  }
}
