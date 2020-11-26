package cn.nihility.mybatis;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.Test;

public class SystemMetaObjectTest {

  @Test
  public void test() {
    InnerClazz clazz = new InnerClazz();

    MetaObject metaObject = SystemMetaObject.forObject(clazz);

    for (String name : metaObject.getGetterNames()) {
      System.out.println(name);
    }
  }


  static class InnerClazz {
    private String name;
    private Integer age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }
  }

}
