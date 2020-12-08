package cn.nihility.mapper.child;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class FilterClassWrapper implements FactoryBean<Object> {

  private Class<?> interfaces;

  public FilterClassWrapper() {
  }

  public FilterClassWrapper(Class<?> interfaces) {
    this.interfaces = interfaces;
  }

  public Class<?> getInterfaces() {
    return interfaces;
  }

  public void setInterfaces(Class<?> interfaces) {
    this.interfaces = interfaces;
  }

  @Override
  public String toString() {
    return "FilterClassWrapper{" +
      "interfaces=" + interfaces +
      '}';
  }

  @Override
  public Object getObject() throws Exception {
    return Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[] {interfaces},
      (proxy, method, args) -> {
        System.out.println("invoke [" + method.getName() + "]");
        if (Object.class.equals(method.getDeclaringClass())) {
          return method.invoke(this, args);
        }
        return null;
      });
  }

  @Override
  public Class<?> getObjectType() {
    return interfaces;
  }
}
