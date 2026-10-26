package com.zhuyue.clockMyGoal.modules.admin.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class PageResponse<T> {
  private Integer pageSize;
  private Integer pageNum;
  private Long total;
  private List<T> data;
}