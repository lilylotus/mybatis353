package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.entity.Flower;
import org.apache.ibatis.annotations.Select;

public interface DuplicateMapper {

    @Select("SELECT id, name_english, name_chinese, age, add_time, update_time FROM flowe WHERE id = #{id}")
    Flower searchById2(String id);

}
