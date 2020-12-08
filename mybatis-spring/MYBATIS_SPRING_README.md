# 源

## mybatis spring 拓展点

### MapperScan / MapperScans 注解

> spring frame 的扩展点， @Import 注解 + ImportBeanDefinitionRegistrar 接口/ ImportSelector 接口

* `MapperScan`

```java
@Import(MapperScannerRegistrar.class)
public @interface MapperScan {}
```

* `MapperScans`

```java
@Import(MapperScannerRegistrar.RepeatingRegistrar.class)
public @interface MapperScans {}
```

* 实现方法

```java
public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                           BeanDefinitionRegistry registry) {
        // registry
        // 注册 @MapperScan 的 basePackages 等等参数到 MapperScannerConfigurer 到 spring bean 容器
    }
}
```

### mybatis mapper 注册 bean 扩展点 (BeanDefinitionRegistryPostProcessor 接口)

> `MapperScannerRegistrar` 注册 `MapperScannerConfigurer` 到 spring bean 容器
> `BeanDefinitionRegistryPostProcessor` 接口继承于 `BeanFactoryPostProcessor` (SPI)
> *mybatis* `MapperScannerConfigurer` (Mapper 扫描配置) 继承 `BeanDefinitionRegistryPostProcessor`


```java
public class MapperScannerConfigurer
    implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        // 添加 类型过滤 / 类型排除 条件
        // 具体的扫描指定 packages 下的 mapper 接口
        // 然后 MapperFactoryBean (继承 FactoryBean<T>) 作为 mapper FactoryBean
    }
}
```

* `ClassPathMapperScanner` 继承 `ClassPathBeanDefinitionScanner`
    为了满足 mybatis mapper 的要求，重写了几个重要的方法

```java
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
  @Override
  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
      Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
      processBeanDefinitions(beanDefinitions);
  }
  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    // 偷梁换柱，把 beanDefinition 中 mapper bean 的内部类变为 MapperFactoryBean 在代理 mapper 接口
  }
  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    // 自定义的选择组件， mybatis 要求所选的为 接口
  }
}
```

### `MapperFactoryBean` 和 `SqlSessionTemplate` 组合

```java
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {
  @Override
  public T getObject() throws Exception {
    return getSqlSession().getMapper(this.mapperInterface);
  }
  public SqlSession getSqlSession() {
    return this.sqlSessionTemplate;
  }
}
```

* 示例

```java
public class MapperScanTest {
  @Test
  public void mapperScan() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfigWithDefaultMapperScanAndRepeat.class);

    GenericBeanDefinition definition = new GenericBeanDefinition();
    definition.setBeanClass(SqlSessionFactoryBean.class);
    definition.getPropertyValues().add("dataSource", new MockDataSource());
    ctx.registerBeanDefinition("sqlSessionFactory", definition);

    ctx.refresh();
    ctx.start();

    Ds2Mapper bean = ctx.getBean(Ds2Mapper.class);
  }
}
```

### SqlSessionFactoryBean 初始化配置 （mybatis 的 Configuration 配置解析）

> `InitializingBean` spring 接口 `void afterPropertiesSet()` 方法执行初始化配置

```java
public class SqlSessionFactoryBean
    implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
  @Override
  public void afterPropertiesSet() throws Exception {
      this.sqlSessionFactory = buildSqlSessionFactory();
  }
  protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
    // 执行正常的 mybatis 配置文件解析 （datasource/executor ...) 和 mapper.xml 解析 [XMLConfigBuilder]
    // SqlSessionFactoryBuilder -> build(Configuration) -> new DefaultSqlSessionFactory(config);
    // 这个和普通的 mybatis 的 SqlSessionFactory 配置流程一致，对上了
    return new SqlSessionFactoryBuilder().build(targetConfiguration);
  }
}
```
