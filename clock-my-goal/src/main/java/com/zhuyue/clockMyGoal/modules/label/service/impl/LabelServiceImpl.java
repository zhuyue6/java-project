package com.zhuyue.clockMyGoal.modules.label.service.impl;

import org.springframework.stereotype.Service;

import com.zhuyue.clockMyGoal.common.constants.*;
import com.zhuyue.clockMyGoal.common.exception.BusinessException;
import com.zhuyue.clockMyGoal.modules.label.dto.request.*;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;
import com.zhuyue.clockMyGoal.modules.label.entity.Label;
import com.zhuyue.clockMyGoal.modules.label.mapper.LabelMapper;
import com.zhuyue.clockMyGoal.modules.label.service.LabelService;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.ArrayList;

@Service("LabelService")
public class LabelServiceImpl implements LabelService {
  @Resource
  private LabelMapper labelMapper;

  private LabelResponse convertToLabelResponse(Label label) {
    if (label == null) {
      return null;
    }
    LabelResponse response = new LabelResponse();
    response.setId(label.getId());
    response.setLabelName(label.getLabelName());
    response.setLabelType(label.getLabelType());
    return response;
  }

  @Override
  public long createLabel(long userId, CreateLabelRequest dto) {
    // 查询相同用户下同一个分类下名称不能重复
    List<LabelResponse> labelList = getLabelListByUserId(userId);
    for (LabelResponse label : labelList) {
      if (label.getLabelName().equals(dto.getLabelName()) && label.getLabelType().equals(dto.getLabelType())) {
        throw new BusinessException(ExceptionCodeEnum.LabelNameAlreadyExists);
      }
    }

    Label label = new Label();
    label.setLabelName(dto.getLabelName());
    label.setLabelType(dto.getLabelType());
    labelMapper.insertLabel(userId, label);
    long labelId = label.getId();

    return labelId;
  }

  @Override
  public void removeLabel(long userId, long id) {
    labelMapper.deleteLabelById(userId, id);
  }

  @Override
  public void updateLabelById(long userId, long id, UpdateLabelRequest dto) {
    Label label = Label.builder()
                    .labelType(dto.getLabelType())
                    .labelName(dto.getLabelName())
                    .build();
    labelMapper.updateLabelById(userId, id, label);
  }

  @Override
  public List<LabelResponse> getLabelListByUserId(long userId) {
    List<Label> labelList = labelMapper.selectLabelByUserId(userId);
    List<LabelResponse> responseList = new ArrayList<>();
    if (labelList == null) {
      return responseList;
    }

    for (Label label : labelList) {
      LabelResponse response = convertToLabelResponse(label);
        responseList.add(response);
    }

    return responseList;
  }

  @Override
  public LabelResponse getLabelById(Long id) {
    if (id == null) {
      return null;
    }
    Label label = labelMapper.selectLabelById(id);
    LabelResponse response = convertToLabelResponse(label);

    return response;
  }

}
