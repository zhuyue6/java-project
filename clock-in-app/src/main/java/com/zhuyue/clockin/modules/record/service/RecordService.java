package com.zhuyue.clockin.modules.record.service;

import com.zhuyue.clockin.modules.record.dto.request.CreateRecordRequest;
import com.zhuyue.clockin.modules.record.dto.response.RecordResponse;
import java.util.List;

public interface RecordService {
  /**
   * 创建打卡记录
   * @param userId 用户ID
   * @param dto 打卡记录请求DTO
   * @return 打卡记录响应DTO
   */
  public long createRecord(Long userId, CreateRecordRequest dto);
  /**
   * 获取用户所有打卡记录
   * @param userId 用户ID
   * @return 打卡记录响应DTO列表
   */
  public List<RecordResponse> getRecords(long userId);
}
