package com.zhuyue.clockin.modules.record.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.annotation.Resource;
import com.zhuyue.clockin.common.interceptor.LoginUser;
import com.zhuyue.clockin.modules.record.dto.request.CreateRecordRequest;
import com.zhuyue.clockin.modules.record.dto.response.RecordResponse;
import com.zhuyue.clockin.modules.record.service.RecordService;
import java.util.List;
import org.springframework.web.bind.annotation.RequestAttribute;


@RestController
@RequestMapping("/record") 
public class RecordController {

  @Resource(name = "RecordService")
  private RecordService recordService;

  @PostMapping("/createRecord")
  public long createRecord(@RequestBody CreateRecordRequest createRecordRequest, @RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return recordService.createRecord(userId, createRecordRequest);
  }

  @GetMapping("/getRecords")
  public List<RecordResponse> getRecords(@RequestAttribute("userInfo") LoginUser loginUser) {
    long userId = loginUser.getUserId();
    return recordService.getRecords(userId);
  }
}
