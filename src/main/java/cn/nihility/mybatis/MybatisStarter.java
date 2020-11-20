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

    private static final String FLOWER_ID = "000005";

    public static void main(String[] args) throws IOException {
        String mybatisConfigLocation = "mybatis/mybatis-config.xml";

        InputStream inputStream = Resources.getResourceAsStream(mybatisConfigLocation);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sessionFactory.openSession(false)) {
            FlowerMapper mapper = session.getMapper(FlowerMapper.class);

            List<Flower> gardenItemList = mapper.searchAll();
            System.out.println(gardenItemList);

            System.out.println("-------------------------------");
            mapper.insertByEntity(generateFlower());
            session.commit();
            System.out.println(mapper.searchById(FLOWER_ID));

            System.out.println("-------------------------------");
            mapper.updateByEntity(generateUpdateFlower());
            session.commit();
            System.out.println(mapper.searchById(FLOWER_ID));

            System.out.println("-------------------------------");
            System.out.println(mapper.deleteById(FLOWER_ID));
            System.out.println(mapper.searchById(FLOWER_ID));

        }
    }

    private static Flower generateUpdateFlower() {
      return new Flower(FLOWER_ID, null, "水仙花-新品种", 100);
    }

    private static Flower generateFlower() {
        return new Flower(FLOWER_ID, "daffodil", "水仙花", 8);
    }

}
