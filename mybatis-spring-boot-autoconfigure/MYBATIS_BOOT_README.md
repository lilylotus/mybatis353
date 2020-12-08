# mybatis spring boot autoconfigure

## 实现自动配置

* spring boot SPI *META-INF/spring.factories* 文件

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration,\
org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
```

## MybatisAutoConfiguration

`MybatisAutoConfiguration` 的配置 + 注解

```java
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class })
public class MybatisAutoConfiguration implements InitializingBean {}
```

* Mybatis 的属性配置
    和 mybatis 中的 `Configuration` 类中所需属性对应

```java
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties {
  public static final String MYBATIS_PREFIX = "mybatis";
  /* 重要的几个属性 */
  /**
   * Location of MyBatis xml config file.
   */
  private String configLocation;
  /**
   * Locations of MyBatis mapper files.
   */
  private String[] mapperLocations;
  /**
   * Packages to search type aliases. (Package delimiters are ",; \t\n")
   */
  private String typeAliasesPackage;
}
```

```yaml
mybatis:
  mapperLocations: classpath:mybatis/mapper/*.xml
  configLocation: classpath:mybatis/mybatis-config.xml
  typeAliasesPackage: com.example.mybatis.dto
```

* 创建一个 SqlSessionFactory Bean (SqlSessionFactoryBean)
* 创建一个 SqlSessionTemplate Bean (SqlSessionTemplate)
* mapper registering configuration or mapper scanning configuration 不存在时自动扫描配置

```java
public class MybatisAutoConfiguration implements InitializingBean {
  @Bean
  @ConditionalOnMissingBean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {}

  @Bean
  @ConditionalOnMissingBean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {}

  @org.springframework.context.annotation.Configuration
  @Import(AutoConfiguredMapperScannerRegistrar.class)
  @ConditionalOnMissingBean({ MapperFactoryBean.class, MapperScannerConfigurer.class })
  public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {}
}
```

### SqlSessionFactoryBean 作用

调用方法 `getObject()` 获取 `SqlSessionFactory` (`DefaultSqlSessionFactory`)
其中做了： 1. mybatis 的 xml 配置文件解析。 2. mapper 接口对应的 xml 配置文件解析 (mapperLocations)

```
new SqlSessionFactoryBuilder().build(Configuration);
  -> return new DefaultSqlSessionFactory(config);
```
