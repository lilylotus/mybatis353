package cn.nihility.mybatis;

import cn.nihility.mybatis.entity.Flower;
import cn.nihility.mybatis.mapper.FlowerMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisStarter {

    private static final String FLOWER_ID = "00009";

    public static void main(String[] args) throws IOException {
        String mybatisConfigLocation = "mybatis/mybatis-config.xml";

        InputStream inputStream = Resources.getResourceAsStream(mybatisConfigLocation);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sessionFactory.openSession()) {
            FlowerMapper mapper = session.getMapper(FlowerMapper.class);
            // FlowerMapper2 mapper2 = session.getMapper(FlowerMapper2.class);

            //insertAndDelete(mapper, session);

            List<Flower> gardenItemList = mapper.searchAll();
            System.out.println(gardenItemList);

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
