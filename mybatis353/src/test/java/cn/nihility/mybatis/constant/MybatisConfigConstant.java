package cn.nihility.mybatis.constant;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * mybatis constant properties
 */
public class MybatisConfigConstant {

  public static final String MYBATIS_CONFIG_LOCATION = "mybatis/mybatis-config.xml";

  public static SqlSessionFactory buildSqlSessionFactory() {
    try {
      return new SqlSessionFactoryBuilder()
        .build(Resources.getResourceAsReader(MybatisConfigConstant.MYBATIS_CONFIG_LOCATION));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

}
