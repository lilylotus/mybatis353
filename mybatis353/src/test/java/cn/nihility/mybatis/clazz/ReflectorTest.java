package cn.nihility.mybatis.clazz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReflectorTest {

    @Test
    public void constructorUse() throws Exception {
        // 默认构造器
        InnerClazz clazz = InnerClazz.class.getDeclaredConstructor().newInstance();
        Assertions.assertNotNull(clazz);

        String property = "constructor";
        clazz.setName(property);
        Assertions.assertEquals(property, clazz.getName());
    }

    @Test
    public void multiConstructor() throws Exception {
        String property = "constructor";
        InnerClazz clazz = InnerClazz.class.getDeclaredConstructor(String.class).newInstance(property);

        Assertions.assertNotNull(clazz);
        Assertions.assertEquals(property, clazz.getName());
    }

    @Test
    public void iterator() {
      List<String> it = new ArrayList<>();
      it.add("one");
      it.add("tow");

      Iterator<String> iterator = it.iterator();
      while (iterator.hasNext()) {
        System.out.println(iterator.next() + " : " + iterator.hasNext());
      }
    }


    static class InnerClazz {
        private String name;

        public InnerClazz() {
        }

        public InnerClazz(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "InnerClazz{" +
                "name='" + name + '\'' +
                '}';
        }
    }

}
