package cn.nihility.auto.entity;

import java.time.LocalDateTime;

public class Subject {

  private String name;
  private Integer id;
  private Integer age;
  private Integer height;
  private Integer weight;
  private Boolean active;
  private LocalDateTime dt;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public LocalDateTime getDt() {
    return dt;
  }

  public void setDt(LocalDateTime dt) {
    this.dt = dt;
  }

  @Override
  public String toString() {
    return "Subject{" +
      "name='" + name + '\'' +
      ", id=" + id +
      ", age=" + age +
      ", height=" + height +
      ", weight=" + weight +
      ", active=" + active +
      ", dt=" + dt +
      '}';
  }
}
