package com.zhuyue.clockMyGoal.modules.label.service;

import java.util.List;

import com.zhuyue.clockMyGoal.modules.label.dto.request.*;
import com.zhuyue.clockMyGoal.modules.label.dto.response.LabelResponse;

public interface LabelService {
  /**
   * 创建标签
   * @param userId 用户ID
   * @param dto 创建标签请求DTO
   * @return 标签ID
   */
  public long createLabel(long userId, CreateLabelRequest dto);

  /**
   * 删除标签
   * @param userId 用户ID
   * @param id 标签ID
   */
  public void removeLabel(long userId, long id);

  /**
   * 更新标签
   * @param userId 用户ID
   * @param id 标签ID
   * @param dto 更新标签请求DTO
   */
  public void updateLabelById(long userId, long id, UpdateLabelRequest dto);

  /**
   * 获取用户所有标签
   * @param userId 用户ID
   * @return 标签列表
   */
  public List<LabelResponse> getLabelListByUserId(long userId);

  /**
   * 获取标签
   * @param id 标签ID
   * @return 标签响应DTO
   */
  public LabelResponse getLabelById(Long id);
}
