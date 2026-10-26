package com.zhuyue.clockMyGoal.modules.label.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.zhuyue.clockMyGoal.common.interceptor.LoginUser;
import com.zhuyue.clockMyGoal.modules.label.dto.request.CreateLabelRequest;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockMyGoal.modules.label.service.LabelService;

@RestController
@RequestMapping("/labels")
@Validated 
public class LabelController {

  @Resource(name = "LabelService")
  private LabelService labelService;

  @PostMapping
  public Long createLabel(@Valid @RequestBody CreateLabelRequest label, @RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return labelService.createLabel(userId, label);
  }

  @DeleteMapping("/{id}")
  public void removeLabel(@PathVariable("id") long id, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    labelService.removeLabel(userId, id);
  }

  @GetMapping
  public List<LabelResponse> getLabelList(@RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return labelService.getLabelListByUserId(userId);
  }

  @GetMapping("/{id}")
  public LabelResponse getLabelById(@PathVariable ("id") long id) {
    return labelService.getLabelById(id);
  }
}
