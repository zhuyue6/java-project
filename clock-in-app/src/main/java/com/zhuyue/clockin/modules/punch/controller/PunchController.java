package com.zhuyue.clockin.modules.punch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.zhuyue.clockin.modules.punch.dto.request.CreateRecordRequest;
import com.zhuyue.clockin.modules.punch.dto.response.CreateRecordResponse;
import com.zhuyue.clockin.modules.punch.service.PunchService;
import jakarta.annotation.Resource;
import com.zhuyue.clockin.common.interceptor.LoginUser;
import org.springframework.web.bind.annotation.RequestAttribute;


@RestController
@RequestMapping("/punch") 
public class PunchController {

  @Resource(name = "PunchService")
  private PunchService punchService;

  @PostMapping("/createRecord")
  public CreateRecordResponse createRecord(@RequestBody CreateRecordRequest record, @RequestAttribute("userInfo") LoginUser loginUser) {
    Long userId = loginUser.getUserId();
    return punchService.createRecord(userId, record);
  }
}
