package cn.nihility.mybatis;

import cn.nihility.mybatis.entity.Flower;
import cn.nihility.mybatis.mapper.FlowerMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class MybatisStarter {

  private static final String FLOWER_ID = "00009";
  private static final String MYBATIS_CONFIG_LOCATION = "mybatis/mybatis-config.xml";

  private static SqlSessionFactory buildSqlSessionFactory() throws IOException {
    return new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(MYBATIS_CONFIG_LOCATION));
  }

  public static void main(String[] args) throws IOException {
    SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory();

    try (SqlSession session = sqlSessionFactory.openSession()) {

      final FlowerMapper mapper = session.getMapper(FlowerMapper.class);

      final Flower fw = new Flower("202108100001", "typeHandler", "类型处理", 20);
      fw.setMillisTime(LocalDateTime.now());
      final Integer integer = mapper.insertTypeHandlerByEntity(fw);
      System.out.println(integer);
      session.commit();

      final Flower flower = mapper.searchFlowerTypeHandlerById("202108100001");
      System.out.println(flower);

      /*Map<String, Object> map = mapper.searchAllAsMap();
      map.forEach((k, v) -> System.out.println(k + " : " + v));*/

      /*List<Flower> flowerList = mapper.searchLikeEnglishName2("lily");
      flowerList.forEach(System.out::println);*/

      /*flowerList = mapper.searchLikeEnglishName("o");
      flowerList.forEach(System.out::println);*/

      /*Integer updateCnt = mapper.updateNameById("00001", "百合new");
      System.out.println("update cnt [" + updateCnt + "]");
      session.commit();*/

      /*final Flower flower = new Flower("auto", "auto", 20);
      final Integer integer = mapper.insertNoAutoGenerateId(flower);
      session.commit();
      System.out.println(flower);
      System.out.println(integer);*/

      /*final AutoIncrementEntity entity = new AutoIncrementEntity("自增");
      final Integer resultEntity = mapper.insertMysqlAutoGenerateId(entity);
      session.commit();
      System.out.println(entity);
      System.out.println(resultEntity);*/

      /*final FlowerMapper mapper = session.getMapper(FlowerMapper.class);
      Gardener gardeners = mapper.selectGardenerByIdWithResultMap("00001");
      System.out.println(gardeners);

      Gardener gardenerSelect = mapper.selectGardenerByIdWithResultMapSelect("00001");
      System.out.println(gardenerSelect);*/
    }

  }

  public static void main1(String[] args) throws IOException {
    String mybatisConfigLocation = "mybatis/mybatis-config.xml";

    InputStream inputStream = Resources.getResourceAsStream(mybatisConfigLocation);
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    try (SqlSession session = sessionFactory.openSession()) {
      FlowerMapper mapper = session.getMapper(FlowerMapper.class);
      // FlowerMapper2 mapper2 = session.getMapper(FlowerMapper2.class);

      //insertAndDelete(mapper, session);

      List<Flower> gardenItemList = mapper.searchAll();
      System.out.println(gardenItemList);
      session.commit();

          /*Map<String, Object> flowerMap = mapper.searchByIdAsMap(FLOWER_ID);
          System.out.println(flowerMap);

          System.out.println("----------------------------------");
          Map<String, Object> mapList = mapper.searchAllAsMap();
          System.out.println(mapList);*/

           /* System.out.println("-------------------------------");
            mapper.insertByEntity(generateFlower());
            session.commit();
            System.out.println(mapper2.searchById(FLOWER_ID));

            System.out.println("-------------------------------");
            mapper.updateByEntity(generateUpdateFlower());
            session.commit();
            System.out.println(mapper.searchById(FLOWER_ID));

            System.out.println("-------------------------------");
            mapper.updateNameById(FLOWER_ID, "测试修改");
            session.commit();
            System.out.println(mapper.searchById(FLOWER_ID));

            System.out.println("-------------------------------");
            System.out.println(mapper.deleteById(FLOWER_ID));
            session.commit();
            System.out.println(mapper.searchById(FLOWER_ID));*/

    }
  }

  private static void insertAndDelete(FlowerMapper mapper, SqlSession session) {

    Flower flower = generateFlower();
    mapper.insertByEntity(flower);
    session.commit();
    System.out.println(mapper.searchById(FLOWER_ID));

    flower.setNameChinese("修改值");
    mapper.updateByEntity(flower);
    session.commit();

    mapper.updateColumnById(FLOWER_ID, "name_chinese", "change值");
    session.commit();

    System.out.println("-------------------------------");
    System.out.println(mapper.deleteById(FLOWER_ID));
    session.commit();
  }

  private static Flower generateUpdateFlower() {
    return new Flower(FLOWER_ID, null, "水仙花-新品种", 100);
  }

  private static Flower generateFlower() {
    return new Flower(FLOWER_ID, "daffodil", "水仙花", 8);
  }

}
