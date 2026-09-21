package com.zhuyue.clockin.modules.label.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.zhuyue.clockin.modules.label.dto.request.CreateLabelRequest;
import com.zhuyue.clockin.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockin.modules.label.service.LabelService;
import jakarta.annotation.Resource;
import com.zhuyue.clockin.common.interceptor.LoginUser;
import com.zhuyue.clockin.modules.label.entity.Label;
import java.util.List;


@RestController
@RequestMapping("/label") 
public class LabelController {

  @Resource(name = "LabelService")
  private LabelService labelService;

  @PostMapping("/createLabel")
  public Long createLabel(@RequestBody CreateLabelRequest label, @RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return labelService.createLabel(userId, label);
  }

  @GetMapping("/getLabelList")
  public List<LabelResponse> getLabelList(@RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return labelService.getLabelListByUserId(userId);
  }

  @GetMapping("/getLabelById")
  public LabelResponse getLabelById(@RequestParam("id") Long id) {
    return labelService.getLabelById(id);
  }
}
