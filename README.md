# mybatis353
mybatis 3.5.3 source code learn note

Essentials
----------

* [See the docs](http://mybatis.github.io/mybatis-3)
* [Download Latest](https://github.com/mybatis/mybatis-3/releases)
* [Download Snapshot](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/mybatis/)


---
# Mybatis Mapper Dynamic SQL

## if

`<if test="arg != null"> statement </if>`
支持多个条件连接: *and*, *or*

## choose, when, otherwise

```
<choose>
  <when test="arg != null"> statement </when>
  <when test="arg1 != null"> statement1 </when>
  <otherwise> otherwise_statement </otherwise>
</choose>
```

## trim, where, set

### where

* where 写法，防止前 AND | OR
```
<select id="findActiveBlogLike" resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

* trim 替代写法：
```
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

### update

* update 写法，防止 , 号

```
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

* trim 替代写法
```
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```

## foreach

```
<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
    #{item}
</foreach>
```