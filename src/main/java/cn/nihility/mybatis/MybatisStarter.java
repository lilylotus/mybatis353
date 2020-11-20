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

    public static void main(String[] args) throws IOException {
        String mybatisConfigLocation = "mybatis/mybatis-config.xml";

        InputStream inputStream = Resources.getResourceAsStream(mybatisConfigLocation);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sessionFactory.openSession()) {
            FlowerMapper mapper = session.getMapper(FlowerMapper.class);

            List<Flower> gardenItemList = mapper.searchAll();
            System.out.println(gardenItemList);
        }
    }

}
