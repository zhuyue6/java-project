package com.zhuyue.clockin.modules.label.service;

import com.zhuyue.clockin.modules.label.dto.request.CreateLabelRequest;
import com.zhuyue.clockin.modules.label.dto.response.LabelResponse;
import java.util.List;

public interface LabelService {
  public long createLabel(long userId, CreateLabelRequest dto);
  public List<LabelResponse> getLabelListByUserId(long userId);
  public LabelResponse getLabelById(long id);
}
