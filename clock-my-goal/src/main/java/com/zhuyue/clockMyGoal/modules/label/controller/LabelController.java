package com.zhuyue.clockMyGoal.modules.label.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhuyue.clockMyGoal.common.interceptor.LoginUser;
import com.zhuyue.clockMyGoal.modules.label.dto.request.CreateLabelRequest;
import com.zhuyue.clockMyGoal.modules.label.dto.request.RemoveLabelRequest;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockMyGoal.modules.label.service.LabelService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/label") 
public class LabelController {

  @Resource(name = "LabelService")
  private LabelService labelService;

  @PostMapping("/createLabel")
  public Long createLabel(@Valid @RequestBody CreateLabelRequest label, @RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return labelService.createLabel(userId, label);
  }

  @PostMapping("/removeLabel")
  public void removeLabel(@Valid @RequestBody RemoveLabelRequest removeLabelRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    labelService.removeLabel(userId, removeLabelRequest);
  }

  @GetMapping("/getLabelList")
  public List<LabelResponse> getLabelList(@RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return labelService.getLabelListByUserId(userId);
  }

  @GetMapping("/getLabelById")
  public LabelResponse getLabelById(@Valid @RequestParam("id") Long id) {
    return labelService.getLabelById(id);
  }
}
