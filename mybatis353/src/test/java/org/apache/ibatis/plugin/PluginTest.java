/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.plugin;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PluginTest {

  @Test
  void mapPluginShouldInterceptGet() {
    Map map = new HashMap();
    map = (Map) new AlwaysMapPlugin().plugin(map);
    assertEquals("Always", map.get("Anything"));
  }

  @Test
  void shouldNotInterceptToString() {
    Map map = new HashMap();
    map = (Map) new AlwaysMapPlugin().plugin(map);
    assertNotEquals("Always", map.toString());
  }

  @Test
  void multiPlugin() {
    Map map = new HashMap();
    Map map1 = (Map) new AlwaysMapPlugin().plugin(map);
    Map map2 = (Map) new OkMapPlugin().plugin(map1);
    Object anything0 = map.get("Anything");
    Object anything1 = map1.get("Anything");
    Object anything2 = map2.get("Anything");

    AlwaysMapPlugin plugin = new AlwaysMapPlugin();
    Class<?> declaringClass = plugin.getClass().getDeclaringClass();
    System.out.println(declaringClass);

    System.out.println(plugin.getClass().getSuperclass());

    /*
     * map2.get() -> OkMapPlugin:intercept <- Ok + (map1)invocation.proceed();
     * AlwaysMapPlugin:intercept <- "Always :" + map.get()
     * Ok : Always : null
     */
    System.out.println(anything0);
    System.out.println(anything1);
    System.out.println(anything2);
  }

  @Intercepts({
      @Signature(type = Map.class, method = "get", args = {Object.class})})
  public static class AlwaysMapPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
      return "Always : " + invocation.proceed();
    }

  }

  @Intercepts({
    @Signature(type = Map.class, method = "get", args = {Object.class})})
  public static class OkMapPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
      return "Ok : " + invocation.proceed();
    }

  }

}
