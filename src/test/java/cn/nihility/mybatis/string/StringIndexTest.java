package cn.nihility.mybatis.string;

import org.junit.jupiter.api.Test;

public class StringIndexTest {

  @Test
  public void index() {
    String property = "richType.richMap[key]";

    print("String len", property.length());

    int delim = property.indexOf(".");
    print("delim", delim);
    // richType
    String preSubString = property.substring(0, delim);
    print("preSubString", preSubString);

    // richMap[key]
    String suffixSubString = property.substring(delim + 1, property.length());
    print("suffixSubString", suffixSubString);

    int delim2 = suffixSubString.indexOf("[");
    print("delim2", delim2);
    // richMap
    preSubString = suffixSubString.substring(0, delim2);
    print("preSubString2", preSubString);

    // key
    suffixSubString = suffixSubString.substring(delim2 + 1, suffixSubString.length() - 1);
    print("suffixSubString2", suffixSubString);

  }

  private void print(String pro, Object val) {
    System.out.println(pro + " [" + val + "]");
  }

}
