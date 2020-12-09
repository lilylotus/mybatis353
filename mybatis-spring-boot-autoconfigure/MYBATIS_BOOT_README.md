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
`DefaultSqlSessionFactory` 会注入 `SqlSessionTemplate` 实例当中

```
new SqlSessionFactoryBuilder().build(Configuration);
  -> return new DefaultSqlSessionFactory(config);
```

### mybatis boot (MybatisAutoConfiguration) 实体关联

1. SqlSessionFactoryBean -> getObject -> DefaultSqlSessionFactory
2. SqlSessionTemplate 中注入 1 中的 DefaultSqlSessionFactory

#### mybatis spring 中流程

1. MapperScannerRegistrar 注入 spring 容器 MapperScannerConfigurer bean
2. MapperScannerConfigurer -> 指定 ClassPathMapperScanner 类扫描 mapper 包下所有接口为 BeanDefinitionHolder
3. 后置在把 bean 定义 definition.setBeanClass 为 MapperFactoryBean （偷梁换柱）
4. MapperFactoryBean 获取 mapper 从 SqlSessionTemplate 中获取
5. SqlSessionTemplate 从 getConfiguration() 的 Configuration 中保存解析的 mapper MapperRegistry 中获取
6. MapperProxyFactory 获取具体的 MapperProxy 代理来执行

* mybatis spring
    * @MapperScan 扫描的 mapper -> MapperFactoryBean 需要 SqlSessionTemplate (mapper 加入 spring 容器)
* mybatis spring boot
    * (SqlSessionFactory) -> SqlSessionFactoryBean 注册/解析 -> getObject -> DefaultSqlSessionFactory (mybatis 管理 mapperPoxy)
    * SqlSessionTemplate 需要 -> SqlSessionFactory (mybatis 管理的 SqlSession)
