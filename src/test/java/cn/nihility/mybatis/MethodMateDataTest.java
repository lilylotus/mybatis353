package cn.nihility.mybatis;

import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

public class MethodMateDataTest {

  @Test
  public void testMethodMateData() throws NoSuchMethodException {
    Method method = InnerInterface.class.getDeclaredMethod("searchAll", String.class, String.class);

    Parameter[] parameters = method.getParameters();
    Class<?>[] parameterTypes = method.getParameterTypes();
    Stream.of(parameterTypes).map(Class::getName).forEach(System.out::println);
    System.out.println("------------");

    Annotation[][] annotations = method.getParameterAnnotations();
    for (Annotation[] annotation : annotations) {
      System.out.println("=================");
      for (Annotation at : annotation) {
        if (at instanceof Param) {
          System.out.println(((Param) at).value());
        } else {
          System.out.println(at.getClass().getName());
        }
        System.out.println("-------------");
      }
      System.out.println("=================");
    }

    //arg0
    //arg1

    for (Parameter parameter : parameters) {
      System.out.println(parameter.getName());
    }

    // java.lang.String
    // java.lang.String

    // annotations[0] 是空
    // annotations[1] 是 Param

  }

  interface InnerInterface {
    List<String> searchAll(String id, @Param("name") String name);
  }

}
