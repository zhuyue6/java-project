package com.zhuyue.clockMyGoal.modules.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class UserListRequest {
  @Pattern(regexp = "^[a-zA-Z0-9._]*$", message = "关键词只能包含字母、数字、下划线、点")
  private String keyword;

  @Min(value = 0, message = "状态必须为0或1")
  @Max(value = 1, message = "状态必须为0或1")
  private Integer status;
  
  @NotNull(message = "页码不能为空")
  private Integer pageNum;
  @NotNull(message = "每页条数不能为空")
  private Integer pageSize;
}