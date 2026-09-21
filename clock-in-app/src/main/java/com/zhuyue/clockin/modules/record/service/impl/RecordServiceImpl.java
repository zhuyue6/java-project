package com.zhuyue.clockin.modules.record.service.impl;

import org.springframework.stereotype.Service;
import com.zhuyue.clockin.modules.record.dto.request.CreateRecordRequest;
import com.zhuyue.clockin.modules.record.dto.response.RecordResponse;
import com.zhuyue.clockin.modules.record.entity.Record;
import com.zhuyue.clockin.modules.record.mapper.RecordMapper;
import com.zhuyue.clockin.modules.record.service.RecordService;
import com.zhuyue.clockin.modules.label.service.LabelService;
import com.zhuyue.clockin.modules.label.dto.response.LabelResponse;
import java.util.List;
import jakarta.annotation.Resource;
import java.util.ArrayList;

@Service("RecordService")
public class RecordServiceImpl implements RecordService {
  @Resource
  private RecordMapper recordMapper;
  @Resource
  private LabelService labelService;

  @Override
  public long createRecord(Long userId, CreateRecordRequest dto) {
    Record record = Record.builder()
        .clockInTime(dto.getClockInTime())
        .clockOutTime(dto.getClockOutTime())
        .remark(dto.getRemark())
        .labelId(dto.getLabelId())
        .build();

    recordMapper.insertRecord(userId, record);

    long recordId = record.getId();
    return recordId;
  }

  @Override
  public List<RecordResponse> getRecords(long userId) {
    List<Record> recordList = recordMapper.selectRecordByUserId(userId);
    List<LabelResponse> labelList = labelService.getLabelListByUserId(userId);
    List<RecordResponse> responseList = new ArrayList<>();

    for (Record record : recordList) {
      LabelResponse matchLabel = labelList.stream().filter(r -> r.getId() == record.getLabelId()).findFirst().orElse(null);

      RecordResponse response = RecordResponse.builder()
        .id(record.getId())
        .clockInTime(record.getClockInTime())
        .clockOutTime(record.getClockOutTime())
        .remark(record.getRemark())
        .labelName(matchLabel != null ? matchLabel.getLabelName() : null)
        .labelType(matchLabel != null ? matchLabel.getLabelType() : null)
        .build();
        responseList.add(response);
    }

    return responseList;
  }
}
