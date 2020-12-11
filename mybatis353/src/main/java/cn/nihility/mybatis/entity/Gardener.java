package cn.nihility.mybatis.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 园丁，同时管理多种花
 */
public class Gardener {

  private String id;
  private String name;
  private Integer age;
  private LocalDateTime addTime;
  private LocalDateTime updateTime;

  /**
   * 伙伴
   */
  private Gardener partner;
  private List<Flower> flowers;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public LocalDateTime getAddTime() {
    return addTime;
  }

  public void setAddTime(LocalDateTime addTime) {
    this.addTime = addTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public List<Flower> getFlowers() {
    return flowers;
  }

  public void setFlowers(List<Flower> flowers) {
    this.flowers = flowers;
  }

  public Gardener getPartner() {
    return partner;
  }

  public void setPartner(Gardener partner) {
    this.partner = partner;
  }

  @Override
  public String toString() {
    return "Gardener{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", age=" + age +
      ", addTime=" + addTime +
      ", updateTime=" + updateTime +
      ", flowers=" + flowers +
      ", partner=" + partner +
      '}';
  }


}
