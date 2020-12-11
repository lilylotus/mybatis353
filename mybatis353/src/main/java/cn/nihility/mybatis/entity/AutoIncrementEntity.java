package cn.nihility.mybatis.entity;

import java.time.LocalDateTime;

public class AutoIncrementEntity {

  private Integer id;
  private String name;
  private LocalDateTime addTime = LocalDateTime.now();
  private LocalDateTime updateTime = LocalDateTime.now();

  public AutoIncrementEntity() {
  }

  public AutoIncrementEntity(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  @Override
  public String toString() {
    return "AutoIncrementEntity{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", addTime=" + addTime +
      ", updateTime=" + updateTime +
      '}';
  }
}
