package com.zhuyue.clockin.modules.punch.service.impl;

import org.springframework.stereotype.Service;
import com.zhuyue.clockin.modules.punch.service.PunchService;
import com.zhuyue.clockin.modules.punch.dto.request.CreateRecordRequest;
import com.zhuyue.clockin.modules.punch.dto.response.CreateRecordResponse;

import jakarta.annotation.Resource;
import com.zhuyue.clockin.modules.punch.mapper.PunchMapper;
import com.zhuyue.clockin.modules.punch.entity.Punch;

@Service("PunchService")
public class PunchServiceImpl implements PunchService {
  @Resource
  private PunchMapper punchMapper;

  @Override
  public CreateRecordResponse createRecord(Long userId, CreateRecordRequest dto) {
    Punch punch = Punch.builder()
        .userId(userId)
        .clockInTime(dto.getClockInTime())
        .clockOutTime(dto.getClockOutTime())
        .remark(dto.getRemark())
        .build();
    punchMapper.createRecord(userId, punch);

    CreateRecordResponse response = new CreateRecordResponse();
    response.setId(punch.getId());
    response.setClockInTime(punch.getClockInTime());
    response.setClockOutTime(punch.getClockOutTime());
    response.setRemark(punch.getRemark());
    return response;
  }
}
