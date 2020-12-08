package cn.nihility.mapper;

import cn.nihility.mapper.child.FilterClassWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

@Configuration
@Import(ScannerConfig.class)
public class ScannerConfig implements ApplicationContextAware, ImportBeanDefinitionRegistrar {

  private final static Logger log = LoggerFactory.getLogger(ScannerConfig.class);
  private ApplicationContext applicationContext;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry) {
      @Override
      protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
          definition = (GenericBeanDefinition) holder.getBeanDefinition();
          String beanClassName = definition.getBeanClassName();
          System.out.println(beanClassName);

          // 偷梁换柱，改变 bean 的内部实体类和添加参数
          definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
          definition.setBeanClass(FilterClassWrapper.class);

          definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
          definition.setLazyInit(false);
        }
        return beanDefinitions;
      }

      /**
       * 自定义是否加入 candidates 组件中，重写 ClassPathScanningCandidateComponentProvider
       */
      @Override
      protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
      }
    };
    scanner.setResourceLoader(applicationContext);
    scanner.addIncludeFilter(((metadataReader, metadataReaderFactory) -> {
      String className = metadataReader.getClassMetadata().getClassName();
      log.info("Include Filter Class Name [{}]", className);
      System.out.println("Include Filter Class Name [" + className + "]");
      return className.contains("IncludeFilterClass");
    }));

    scanner.scan("cn.nihility.mapper");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
