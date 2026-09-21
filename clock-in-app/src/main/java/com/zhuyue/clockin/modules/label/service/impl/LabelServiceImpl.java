package com.zhuyue.clockin.modules.label.service.impl;

import org.springframework.stereotype.Service;
import com.zhuyue.clockin.modules.label.service.LabelService;
import com.zhuyue.clockin.modules.label.dto.request.CreateLabelRequest;

import jakarta.annotation.Resource;
import java.util.List;
import com.zhuyue.clockin.modules.label.mapper.LabelMapper;
import com.zhuyue.clockin.modules.label.entity.Label;
import com.zhuyue.clockin.common.exception.BusinessException;
import com.zhuyue.clockin.common.constants.*;
import com.zhuyue.clockin.modules.label.dto.response.LabelResponse;
import java.util.ArrayList;

@Service("LabelService")
public class LabelServiceImpl implements LabelService {
  @Resource
  private LabelMapper labelMapper;

  @Override
  public long createLabel(long userId, CreateLabelRequest dto) {
    // 查询相同用户下同一个分类下名称不能重复
    List<LabelResponse> labelList = getLabelListByUserId(userId);
    for (LabelResponse label : labelList) {
      if (label.getLabelName().equals(dto.getLabelName()) && label.getLabelType().equals(dto.getLabelType())) {
        throw new BusinessException(ExceptionCodeEnum.LabelNameAlreadyExists.getCode(), ExceptionCodeEnum.LabelNameAlreadyExists.getMessage());
      }
    }

    Label label = Label.builder()
      .labelName(dto.getLabelName())
      .labelType(dto.getLabelType())
      .build();


    labelMapper.insertLabel(userId, label);
    long labelId = label.getId();

    return labelId;
  }

  @Override
  public List<LabelResponse> getLabelListByUserId(long userId) {
    List<Label> labelList = labelMapper.selectLabelByUserId(userId);
    List<LabelResponse> responseList = new ArrayList<>();

    for (Label label : labelList) {
      LabelResponse response = LabelResponse.builder()
        .id(label.getId())
        .labelName(label.getLabelName())
        .labelType(label.getLabelType())
        .build();
        responseList.add(response);
    }

    return responseList;
  }

  @Override
  public LabelResponse getLabelById(long id) {
    Label label = labelMapper.selectLabelById(id);

    LabelResponse response = LabelResponse.builder()
      .id(label.getId())
      .labelName(label.getLabelName())
      .labelType(label.getLabelType())
      .build();

    return response;
  }
}
