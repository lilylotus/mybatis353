package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.entity.Flower;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FlowerMapper {

  Flower searchById(String id);

  @MapKey("id")
  Map<String, Object> searchByIdAsMap(String id);

  List<Flower> searchAll();

  @MapKey("id")
  Map<String, Object> searchAllAsMap();

  Integer insertByEntity(Flower flower);

  Integer updateByEntity(Flower flower);

  Integer updateNameById(String id, String name);

  Integer updateColumnById(String id, @Param("field") String field, Object data);

  Integer deleteById(String id);

}
