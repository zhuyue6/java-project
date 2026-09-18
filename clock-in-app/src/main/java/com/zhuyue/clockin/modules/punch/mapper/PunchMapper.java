package com.zhuyue.clockin.modules.punch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.zhuyue.clockin.modules.punch.entity.Punch;

@Mapper
public interface PunchMapper {
  // insert 返回影响行数；自增 id 回写到 punch.id
  int createRecord(@Param("userId") Long userId, @Param("punch") Punch punch);
  Punch selectRecordByUserId(@Param("userId") Long userId);
}
