package cn.nihility.auto.mapper;

import cn.nihility.auto.entity.Subject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubjectMapper {

  @Select("SELECT id, name, age, height, weight, active, dt FROM subject WHERE id = #{id}")
  Subject querySubjectById(Integer id);

  List<Subject> selectAll();

}
