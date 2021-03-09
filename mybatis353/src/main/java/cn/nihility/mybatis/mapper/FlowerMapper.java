package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.entity.AutoIncrementEntity;
import cn.nihility.mybatis.entity.Flower;
import cn.nihility.mybatis.entity.Gardener;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FlowerMapper {

  Flower searchById(String id);

  @MapKey("id")
  Map<String, Object> searchByIdAsMap(String id);

  List<Flower> searchAll();

  List<Flower> searchLikeEnglishName(String name);

  List<Flower> searchLikeEnglishName2(String name);

  @MapKey("id")
  Map<String, Object> searchAllAsMap();

  Integer insertByEntity(Flower flower);

  Integer updateByEntity(Flower flower);

  Integer updateNameById(String id, String name);

  Integer updateColumnById(String id, @Param("field") String field, Object data);

  Integer deleteById(String id);

  Gardener selectGardenerByIdWithResultMap(String id);

  Gardener selectGardenerByIdWithResultMapSelect(String id);

  Integer insertMysqlAutoGenerateId(AutoIncrementEntity entity);

  Integer insertNoAutoGenerateId(Flower entity);

}
