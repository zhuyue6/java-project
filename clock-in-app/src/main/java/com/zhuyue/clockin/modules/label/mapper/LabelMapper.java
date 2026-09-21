package com.zhuyue.clockin.modules.label.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.zhuyue.clockin.modules.label.entity.Label;

@Mapper
public interface LabelMapper {
  // insert 返回影响行数；自增 id 回写到 label.id
  void insertLabel(@Param("userId") long userId, @Param("label") Label label);
  List<Label> selectLabelByUserId(@Param("userId") long userId);
  Label selectLabelById(@Param("id") long id);
}
