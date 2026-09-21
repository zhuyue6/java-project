package com.zhuyue.clockin.modules.record.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.zhuyue.clockin.modules.record.entity.Record;
import java.util.List;

@Mapper
public interface RecordMapper {
  // insert 返回影响行数；自增 id 回写到 record.id
  void insertRecord(@Param("userId") long userId, @Param("record") Record record);
  List<Record> selectRecordByUserId(@Param("userId") long userId);
}
