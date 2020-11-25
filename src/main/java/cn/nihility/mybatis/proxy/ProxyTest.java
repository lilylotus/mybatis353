package cn.nihility.mybatis.proxy;

import cn.nihility.mybatis.mapper.FlowerMapper;

import java.lang.reflect.Proxy;

public class ProxyTest {

  public static void main(String[] args) {
    ProxyTest proxyTest = new ProxyTest();
    proxyTest.proxy();
  }


  public void proxy() {
    Object obj = Proxy.newProxyInstance(ProxyTest.class.getClassLoader(),
      new Class<?>[]{FlowerMapper.class},
      ((proxy, method, args1) -> {
        if (Object.class.equals(method.getDeclaringClass())) {
          System.out.println("Object Method [" + method.getName() + "]");
          return method.invoke(this, args1);
        }
        return null;
      }));

    System.out.println(obj);
    System.out.println("---------------");
    System.out.println(obj.getClass());
    System.out.println("---------------");
    System.out.println(obj.getClass().getName());

  }

  @Override
  public String toString() {
    return "sssssss";
  }
}
