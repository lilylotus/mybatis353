package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.entity.Flower;

import java.util.List;

public interface FlowerMapper {

    Flower searchById(String id);

    List<Flower> searchAll();

    Integer insertByEntity(Flower flower);

    Integer updateByEntity(Flower flower);

    Integer deleteById(String id);

}
