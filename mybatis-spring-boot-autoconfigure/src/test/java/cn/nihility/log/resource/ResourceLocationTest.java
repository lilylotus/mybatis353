package cn.nihility.log.resource;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceLocationTest {

  @Test
  public void stream() {
    final String[] location = null;
    Integer[] array = Stream.of(Optional.ofNullable(location).orElse(new String[0]))
      .flatMap(local -> Stream.of(splitLocation(local))).toArray(Integer[]::new);

    System.out.println(Stream.of(array).map(Object::toString).collect(Collectors.joining(",")));
  }

  private Integer[] splitLocation(String local) {
    Integer[] array = new Integer[local.length()];
    for (int i = 0; i < local.length(); i++) {
      array[i] = (int) local.charAt(i);
    }
    return array;
  }

}
