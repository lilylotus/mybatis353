package cn.nihility.mapper;

import cn.nihility.mapper.child.ExcludeFilterClass;
import cn.nihility.mapper.child.IncludeFilterClass;
import com.mockrunner.mock.jdbc.MockDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.mapper.ds1.AppConfigWithDefaultMapperScanAndRepeat;
import org.mybatis.spring.annotation.mapper.ds2.Ds2Mapper;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

  @Test
  public void classScanner() {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(ScannerConfig.class);

    ctx.refresh();
    ctx.start();

    System.out.println(ctx.getBean("includeFilterClass"));

    /*Assertions.assertThrows(NoSuchBeanDefinitionException.class,
      () -> ctx.getBean("excludeFilterClass"),
      "No bean named 'excludeFilterClass' available");*/

    Assertions.assertNotNull(ctx.getBean(ExcludeFilterClass.class));
    Assertions.assertNotNull(ctx.getBean("ExcludeFilterClass"));

    System.out.println(ctx.getBean(IncludeFilterClass.class));

      /*ctx.getBean("excludeFilterClass");
      Assertions.fail("No bean named 'excludeFilterClass' available");*/
  }

}
