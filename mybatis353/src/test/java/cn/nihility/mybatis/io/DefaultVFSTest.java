package cn.nihility.mybatis.io;

import org.apache.ibatis.io.DefaultVFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

class DefaultVFSTest {

  @Test
  void testVFS() throws IOException {
    final DefaultVFS vfs = new DefaultVFS();
    final URL url = new URL("file:/E:/temporary/upgrade/uim/lib/tomcat-embed-core-9.0.17.jar");
    final List<String> list = vfs.list(url, "/META-INF/");
    Assertions.assertEquals(2, list.size());
    list.forEach(item -> System.out.println(item));
  }

}
