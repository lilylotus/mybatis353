package cn.nihility.mybatis.mapper;

import cn.nihility.mybatis.constant.MybatisConfigConstant;
import cn.nihility.mybatis.entity.Flower;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BatchInsertTest {

  private static SqlSessionFactory sqlSessionFactory;
  private final static int LOOP_COUNT = 10000;

  @BeforeAll
  public static void initSqlSessionFactory() {
    try {
      sqlSessionFactory = new SqlSessionFactoryBuilder()
        .build(Resources.getResourceAsReader(MybatisConfigConstant.MYBATIS_CONFIG_LOCATION));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static SqlSession getSqlSession() {
    return null == sqlSessionFactory ? null : sqlSessionFactory.openSession();
  }

  @Test
  public void testInsert() {
    try (SqlSession sqlSession = getSqlSession()) {
      assert sqlSession != null;

      final FlowerMapper mapper = sqlSession.getMapper(FlowerMapper.class);
      final Flower flower = buildFlower();

      final long start = System.currentTimeMillis();
      mapper.insertFlowerEntity(flower);
      System.out.println("duration [" + (System.currentTimeMillis() - start) + "]");

      // 247
      sqlSession.commit();
      sqlSession.clearCache();
      sqlSession.flushStatements();

    }
  }

  @Test
  public void testLoopInsert() {
    try (SqlSession sqlSession = getSqlSession()) {
      assert sqlSession != null;

      final FlowerMapper mapper = sqlSession.getMapper(FlowerMapper.class);
      sqlSession.commit(false);
      List<Flower> flowerListLoop = buildFlowerList(LOOP_COUNT, "flowerListLoop");

      final long start = System.currentTimeMillis();
      for (int i = 0; i < LOOP_COUNT; i++) {
        mapper.insertFlowerEntity(flowerListLoop.get(i));
        sqlSession.commit();
      }
      System.out.println("testLoopInsert cnt [" + LOOP_COUNT + "] duration [" + (System.currentTimeMillis() - start) + "]");

    }
  }

  @Test
  public void testMybatisBatch() {
    try (SqlSession sqlSession = getSqlSession()) {
      assert sqlSession != null;

      final FlowerMapper mapper = sqlSession.getMapper(FlowerMapper.class);
      sqlSession.commit(false);
      List<Flower> flowerListBatch = buildFlowerList(LOOP_COUNT, "flowerListBatch");

      final long start = System.currentTimeMillis();
      for (int i = 0; i < LOOP_COUNT; i++) {
        mapper.insertFlowerEntity(flowerListBatch.get(i));
        if (i % 1000 == 999) {
          sqlSession.commit();
          sqlSession.clearCache();
          sqlSession.flushStatements();
        }
      }
      sqlSession.commit();
      sqlSession.clearCache();
      sqlSession.flushStatements();
      System.out.println("testMybatisBatch cnt [" + LOOP_COUNT + "] duration [" + (System.currentTimeMillis() - start) + "]");

    }
  }

  @Test
  public void testMybatisForeach() {
    try (SqlSession sqlSession = getSqlSession()) {
      assert sqlSession != null;

      final FlowerMapper mapper = sqlSession.getMapper(FlowerMapper.class);
      sqlSession.commit(false);
      List<Flower> flowerListForeach = buildFlowerList(LOOP_COUNT, "flowerListForeach");

      final long start = System.currentTimeMillis();
      mapper.insertFlowerList(flowerListForeach);
      sqlSession.commit();
      sqlSession.clearCache();
      sqlSession.flushStatements();
      System.out.println("testMybatisForeach cnt [" + LOOP_COUNT + "] duration [" + (System.currentTimeMillis() - start) + "]");

    }
  }

  public static Flower buildFlower() {
    Random random = new Random(System.currentTimeMillis());
    Flower flower = new Flower();
    String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    flower.setId(uuid);
    flower.setNameChinese("中文花名-" + uuid.substring(0, 6));
    flower.setNameEnglish("englishFlowerName-" + uuid.substring(6, 12));
    flower.setAge(random.nextInt(11));
    return flower;
  }

  public static List<Flower> buildFlowerList(int len, String suffix) {
    List<Flower> flowerList = new ArrayList<>(len);
    Random random = new Random(System.currentTimeMillis());

    for (int i = 0; i < len; i++) {
      Flower flower = new Flower();
      String uuid = UUID.randomUUID().toString().replace("-", "") + "-" + suffix;
      flower.setId(uuid);
      flower.setNameChinese("中文花名-" + uuid.substring(0, 6));
      flower.setNameEnglish("englishFlowerName-" + uuid.substring(6, 12));
      flower.setAge(random.nextInt(11));
      flowerList.add(flower);
    }

    return flowerList;
  }

}
