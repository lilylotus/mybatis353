package cn.nihility.auto;

import cn.nihility.auto.entity.Subject;
import cn.nihility.auto.mapper.SubjectMapper;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MybatisAutoConfigureTest {

  private static final Logger log = LoggerFactory.getLogger(MybatisAutoConfigureTest.class);

  private AnnotationConfigApplicationContext context;
  private static UnpooledDataSource dataSource;

  @BeforeAll
  static void initDatasource() throws IOException, SQLException {
    dataSource = new UnpooledDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:flower", "sa", "");
    BaseDataTest.runScript(dataSource, "cn/nihility/auto/CreateDB.sql");
  }

  @BeforeEach
  void init() {
    this.context = new AnnotationConfigApplicationContext();
  }

  @AfterEach
  void closeContext() {
    if (this.context != null) {
      this.context.close();
    }
  }

  @Test
  public void mybatisAutoConfig() {
    TestPropertyValues.of("mybatis.config-location:mybatis/mybatis-config.xml",
      "mybatis.check-config-location=true", "mybatis.mapper-locations:cn/nihility/auto/mapper/*.xml",
      "mybatis.type-aliases-package:cn.nihility.auto.entity")
      .applyTo(this.context);

    context.register(MybatisMapperScanConfig.class, MybatisDataSourceConfig.class,
      MybatisAutoConfiguration.class, MybatisAutoQueryConfig.class);
    context.refresh();

    SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
    Assertions.assertNotNull(sqlSessionFactory);

    MapperRegistry mapperRegistry = sqlSessionFactory.getConfiguration().getMapperRegistry();
    Assertions.assertEquals(1, mapperRegistry.getMappers().size());

    SqlSessionTemplate sqlSessionTemplate = context.getBean(SqlSessionTemplate.class);
    Assertions.assertNotNull(sqlSessionTemplate);

    SubjectMapper mapper = mapperRegistry.getMapper(SubjectMapper.class, sqlSessionTemplate.getSqlSessionFactory().openSession());
    Assertions.assertNotNull(mapper);

    SubjectMapper subjectMapper = context.getBean(SubjectMapper.class);
    Assertions.assertNotNull(subjectMapper);
    log.info("subjectMapper [{}]", subjectMapper);

    List<Subject> subjects = subjectMapper.selectAll();
    Assertions.assertNotNull(subjects);
    Assertions.assertEquals(3, subjects.size());


    MybatisAutoQueryConfig queryConfig = context.getBean(MybatisAutoQueryConfig.class);
    Assertions.assertNotNull(queryConfig);

    List<Subject> subjects1 = queryConfig.selectAll();
    Assertions.assertEquals(3, subjects1.size());

    Subject subject = queryConfig.selectById(1);
    Assertions.assertNotNull(subject);
    Assertions.assertEquals("a", subject.getName());
  }


  @Test
  public void testDatasource() throws IOException, SQLException {
    UnpooledDataSource dataSource = new UnpooledDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:flower", "sa", "");
    BaseDataTest.runScript(dataSource, "cn/nihility/auto/CreateDB.sql");

    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM subject");
      statement.execute();
      ResultSet resultSet = statement.getResultSet();
      while (resultSet.next()) {
        System.out.println(resultSet.getString("name"));
      }
    }
  }


  static class BaseDataTest {
    public static void runScript(DataSource ds, String resource) throws IOException, SQLException {
      try (Connection connection = ds.getConnection()) {
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setAutoCommit(true);
        runner.setStopOnError(false);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runScript(runner, resource);
      }
    }
    public static void runScript(ScriptRunner runner, String resource) throws IOException, SQLException {
      try (Reader reader = Resources.getResourceAsReader(resource)) {
        runner.runScript(reader);
      }
    }
  }

  @Configuration
  @MapperScan(basePackages = {"cn.nihility.auto.mapper"})
  /*@EnableAutoConfiguration*/
  static class MybatisMapperScanConfig { }

  @Configuration
  static class MybatisDataSourceConfig {
    @Bean
    public DataSource dataSource() {
      return dataSource;
    }
  }

  @Configuration
  static class MybatisAutoQueryConfig {
    private SubjectMapper subjectMapper;

    public MybatisAutoQueryConfig(SubjectMapper subjectMapper) {
      this.subjectMapper = subjectMapper;
    }

    public List<Subject> selectAll() {
      return subjectMapper.selectAll();
    }

    public Subject selectById(Integer id) {
      return subjectMapper.querySubjectById(id);
    }
  }

}
