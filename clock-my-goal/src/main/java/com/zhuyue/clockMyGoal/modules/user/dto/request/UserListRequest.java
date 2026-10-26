package com.zhuyue.clockMyGoal.modules.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class UserListRequest {
  @Pattern(regexp = "^[a-zA-Z0-9._]+$")
  private String keyword;
  @Min(0)
  @Max(1)
  private Integer status;
  
  @NotNull 
  private Integer pageNum;
  @NotNull
  private Integer pageSize;
}