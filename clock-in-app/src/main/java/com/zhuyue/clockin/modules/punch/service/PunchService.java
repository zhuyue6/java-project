package com.zhuyue.clockin.modules.punch.service;

import com.zhuyue.clockin.modules.punch.dto.request.CreateRecordRequest;
import com.zhuyue.clockin.modules.punch.dto.response.CreateRecordResponse;

public interface PunchService {
  public CreateRecordResponse createRecord(Long userId, CreateRecordRequest dto);
}
