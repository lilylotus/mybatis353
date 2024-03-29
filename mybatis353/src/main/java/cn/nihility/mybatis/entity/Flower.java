package cn.nihility.mybatis.entity;

import java.time.LocalDateTime;

public class Flower {

    private String id;
    private String nameEnglish;
    private String nameChinese;
    private Integer age;
    private LocalDateTime addTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();
    private LocalDateTime millisTime = LocalDateTime.now();

    public Flower() {
    }

    public Flower(String nameEnglish, String nameChinese, Integer age) {
        this.nameEnglish = nameEnglish;
        this.nameChinese = nameChinese;
        this.age = age;
    }

  public Flower(String id, String nameEnglish, String nameChinese, Integer age) {
    this.id = id;
    this.nameEnglish = nameEnglish;
    this.nameChinese = nameChinese;
    this.age = age;
  }

  public Flower(String id, String nameEnglish, String nameChinese, Integer age, LocalDateTime addTime, LocalDateTime updateTime) {
    this.id = id;
    this.nameEnglish = nameEnglish;
    this.nameChinese = nameChinese;
    this.age = age;
    this.addTime = addTime;
    this.updateTime = updateTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNameEnglish() {
    return nameEnglish;
  }

  public void setNameEnglish(String nameEnglish) {
    this.nameEnglish = nameEnglish;
  }

  public String getNameChinese() {
    return nameChinese;
  }

  public void setNameChinese(String nameChinese) {
    this.nameChinese = nameChinese;
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

  public LocalDateTime getMillisTime() {
    return millisTime;
  }

  public void setMillisTime(LocalDateTime millisTime) {
    this.millisTime = millisTime;
  }

  @Override
  public String toString() {
    return "Flower{" +
      "id='" + id + '\'' +
      ", nameEnglish='" + nameEnglish + '\'' +
      ", nameChinese='" + nameChinese + '\'' +
      ", age=" + age +
      ", addTime=" + addTime +
      ", updateTime=" + updateTime +
      ", millisTime=" + millisTime +
      '}';
  }

}
