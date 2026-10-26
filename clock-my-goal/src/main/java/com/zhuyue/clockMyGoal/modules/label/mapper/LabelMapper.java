package com.zhuyue.clockMyGoal.modules.label.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zhuyue.clockMyGoal.modules.label.entity.Label;

import java.util.List;

@Mapper
public interface LabelMapper {
  // insert 返回影响行数；自增 id 回写到 label.id
  void insertLabel(@Param("userId") long userId, @Param("label") Label label);
  void deleteLabelById(@Param("userId") long userId, @Param("id") long id);
  List<Label> selectLabelByUserId(@Param("userId") long userId);
  Label selectLabelById(@Param("id") long id);
}
