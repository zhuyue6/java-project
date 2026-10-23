package com.zhuyue.clockMyGoal.modules.user.dto.response;

import lombok.Data;

@Data
public class UserResponse {
  private long id;
  private String userName;
  private int age;
  private int sex;
  private String avatarUrl;
  private Integer status;
  private String createTime;
  private String updateTime;
}

