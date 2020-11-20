package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.entity.Flower;

import java.util.List;

public interface FlowerMapper {

    Flower searchById(Integer id);

    List<Flower> searchAll();

    Integer updateByEntity(Flower garden);

    Integer deleteById(Integer id);

}
