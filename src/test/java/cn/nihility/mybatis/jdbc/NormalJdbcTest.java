package cn.nihility.mybatis.jdbc;

import cn.nihility.mybatis.entity.Flower;
import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 普通的 JDBC 连接测试
 */
public class NormalJdbcTest {

  private static Connection connection;

  @BeforeAll
  public static void init() throws ClassNotFoundException, SQLException {
    String user = "root";
    String password = "mysql";
    String classDriver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://127.0.0.1:3306/mybatis?serverTime=UTC&serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false";
    Class.forName(classDriver);
    connection = DriverManager.getConnection(url, user, password);
  }

  @Test
  public void selectAll() {
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      statement = connection.prepareStatement("SELECT id, name_english, name_chinese, age, add_time, update_time FROM flower");

      statement.execute();

       resultSet = statement.getResultSet();
       // getFirstResultSet
       while (resultSet == null) {
         if (statement.getMoreResults()) {
           System.out.println("getMoreResults true");
           resultSet = statement.getResultSet();
         } else {
           if (statement.getUpdateCount() == -1) {
             System.out.println("getUpdateCount == -1");
             break;
           }
         }
       }

      List<String> columnNamesLabel = new ArrayList<>();
      List<String> columnNamesName = new ArrayList<>();
      List<String> classNames = new ArrayList<>();
      List<JdbcType> jdbcTypes = new ArrayList<>();

      final List<Object> multipleResults = new ArrayList<>();

       if (resultSet != null) {
         ResultSetMetaData metaData = resultSet.getMetaData();
         int columnCount = metaData.getColumnCount();

         for (int i = 1; i <= columnCount; i++) {
           columnNamesLabel.add(metaData.getColumnLabel(i));
           columnNamesName.add(metaData.getColumnName(i));
           jdbcTypes.add(JdbcType.forCode(metaData.getColumnType(i)));
           classNames.add(metaData.getColumnClassName(i));
         }

         System.out.println(String.join(",", columnNamesLabel));
         System.out.println(String.join(",", columnNamesName));
         System.out.println(String.join(",", classNames));
         System.out.println(jdbcTypes.stream().map(type -> type.TYPE_CODE).map(Object::toString).collect(Collectors.joining(",")));
         System.out.println("--------------------------");

         while (resultSet.next()) {
           multipleResults.add(new Flower(resultSet.getString("id"),
             resultSet.getString("name_english"), resultSet.getString("name_chinese"),
             resultSet.getInt("age"),
             resultSet.getObject("add_time", LocalDateTime.class), resultSet.getObject("update_time", LocalDateTime.class)));

         }

         System.out.println(multipleResults.stream().map(Object::toString).collect(Collectors.joining("\n")));

       }


    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      closeResource(statement, resultSet);
    }
  }


  @Test
  public void insert() {

    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = "INSERT INTO flower(id, name_english, name_chinese, age, add_time, update_time) VALUES (?, ?, ?, ?, ?, ?)";
    String updateSql = "UPDATE flower SET name_english = ?, update_time = ? WHERE ID = ?";
    String deleteSql = "DELETE FROM flower WHERE ID = ?";
    String id = "00011";

    boolean autoCommit = false;
    try {
      autoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
      statement = connection.prepareStatement(sql);
      statement.setObject(1, id, Types.VARCHAR);
      statement.setObject(2, id + "_name_english", Types.VARCHAR);
      statement.setObject(3, id + "_name_chinese", Types.VARCHAR);
      statement.setObject(4, RandomUtils.nextInt(0, 30), Types.INTEGER);
      statement.setObject(5, LocalDateTime.now());
      statement.setObject(6, LocalDateTime.now());

      statement.execute();
      connection.commit();
      int updateCount = statement.getUpdateCount();
      System.out.println("update count = " + updateCount);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        connection.setAutoCommit(autoCommit);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      closeResource(statement, resultSet);
    }

    try {
      autoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
      statement = connection.prepareStatement(deleteSql);
      statement.setObject(1, id, Types.VARCHAR);
      statement.execute();
      connection.commit();
      int updateCount = statement.getUpdateCount();
      System.out.println("delete count = " + updateCount);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        connection.setAutoCommit(autoCommit);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      closeResource(statement, resultSet);
    }
  }



  public void closeResource(Statement statement, ResultSet resultSet) {
    if (null != resultSet) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @AfterAll
  public static void closeConnection() throws SQLException {
    if (null != connection) {
      connection.close();
    }
  }

}
