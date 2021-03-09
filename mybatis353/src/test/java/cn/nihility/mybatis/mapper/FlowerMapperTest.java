package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.entity.Flower;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FlowerMapperTest {

  private static final String MYBATIS_CONFIG_LOCATION = "mybatis/mybatis-config.xml";

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  public static void init() {
    try {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(MYBATIS_CONFIG_LOCATION));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @AfterAll
  public static void destroy() {

  }

  @Test
  public void testLikeStatement() {
    try (final SqlSession session = sqlSessionFactory.openSession()) {
      final FlowerMapper mapper = session.getMapper(FlowerMapper.class);
      final List<Flower> flowers = mapper.searchLikeEnglishName("o");

      Assertions.assertEquals(2, flowers.size());
      System.out.println(flowers.stream().map(Object::toString).collect(Collectors.joining(":")));
    }
  }

  @Test
  public void testLikeStatement2() {
    try (final SqlSession session = sqlSessionFactory.openSession()) {
      final FlowerMapper mapper = session.getMapper(FlowerMapper.class);
      final List<Flower> flowers = mapper.searchLikeEnglishName2("o");

      Assertions.assertEquals(2, flowers.size());
      System.out.println(flowers.stream().map(Object::toString).collect(Collectors.joining(":")));
    }
  }

}
