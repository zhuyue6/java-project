package com.zhuyue.clockMyGoal.modules.label.service;

import java.util.List;

import com.zhuyue.clockMyGoal.modules.label.dto.request.CreateLabelRequest;
import com.zhuyue.clockMyGoal.modules.label.dto.request.RemoveLabelRequest;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;

public interface LabelService {
  public long createLabel(long userId, CreateLabelRequest dto);
  public void removeLabel(long userId, RemoveLabelRequest dto);
  public List<LabelResponse> getLabelListByUserId(long userId);
  public LabelResponse getLabelById(long id);
}
