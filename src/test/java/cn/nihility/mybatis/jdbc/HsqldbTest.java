package cn.nihility.mybatis.jdbc;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

public class HsqldbTest {

  private static UnpooledDataSource ds;

  @BeforeAll
  static void init() throws IOException, SQLException {
    Properties props = Resources.getResourceAsProperties("cn/nihility/mybatis/jdbc/dataSource.properties");
    ds = new UnpooledDataSource();
    ds.setDriver(props.getProperty("driver"));
    ds.setUrl(props.getProperty("url"));
    ds.setUsername(props.getProperty("username"));
    //ds.setPassword(props.getProperty("password"));

    // populate in-memory database
    //BaseDataTest.runScript(ds, "org/apache/ibatis/autoconstructor/CreateDB.sql");

    /*StringBuilder script = new StringBuilder();
    InputStream inputStream = HsqldbTest.class.getClassLoader().getResourceAsStream("cn/nihility/mybatis/jdbc/CreateDB.sql");
    InputStreamReader reader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(reader);

    String lineSeparator = System.getProperty("line.separator", "\n");
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      script.append(line).append(lineSeparator);
    }
    inputStream.close();
    String command = script.toString();

//    System.out.println(command);

    Connection connection = ds.getConnection();
    Statement statement = connection.createStatement();
    boolean hasResults = statement.execute(command);
    print("hasResults " + hasResults);
    while (!(!hasResults && statement.getUpdateCount() == -1)) {
      try (ResultSet rs = statement.getResultSet()) {
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();
        for (int i = 0; i < cols; i++) {
          String name = md.getColumnLabel(i + 1);
          print(name + "\t");
        }
        println("");
        while (rs.next()) {
          for (int i = 0; i < cols; i++) {
            String value = rs.getString(i + 1);
            print(value + "\t");
          }
          println("");
        }
      } catch (SQLException e) {
        println("Error printing results: " + e.getMessage());
      }
      hasResults = statement.getMoreResults();
      print("hasResults " + hasResults);
    }
    statement.close();
    connection.commit();
    connection.close();*/
  }

  private static void print(String msg) {
    System.out.print(msg);
  }

  private static void println(String msg) {
    System.out.println(msg);
  }

  @Test
  public void createTable() throws SQLException {
    Connection connection = ds.getConnection();

    String dropTable = "DROP TABLE subject IF EXISTS;";
    String createTable = "CREATE TABLE subject (\n" +
      "  id     INT NOT NULL,\n" +
      "  name   VARCHAR(20),\n" +
      "  age    INT NOT NULL,\n" +
      "  height INT,\n" +
      "  weight INT,\n" +
      "  active BIT,\n" +
      "  dt     TIMESTAMP\n" +
      ");";
    String insertTable = "INSERT INTO subject VALUES\n" +
      "  (1, 'a', 10, 100, 45, 1, CURRENT_TIMESTAMP),\n" +
      "  (2, 'b', 10, NULL, 45, 1, CURRENT_TIMESTAMP),\n" +
      "  (2, 'c', 10, NULL, NULL, 0, CURRENT_TIMESTAMP);";

    Statement statement = connection.createStatement();
    statement.execute(dropTable);
    statement.execute(createTable);
    statement.execute(insertTable);

    connection.commit();
  }

  @Test
  public void query() throws SQLException {
    Connection connection = ds.getConnection();

    String sql = "SELECT id, name, age, height, weight, active, dt FROM subject";

    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {

      statement = connection.prepareStatement(sql);
      statement.execute();

      resultSet = statement.getResultSet();

      while (resultSet.next()) {
        System.out.println(resultSet.getInt("id") + ":" + resultSet.getString("name") + ":" +
          resultSet.getInt("age") + ":" + resultSet.getInt("height") + ":" +
          resultSet.getInt("weight") + ":" + resultSet.getInt("active") + ":" +
          resultSet.getObject("dt", LocalDateTime.class));
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }
      if (null != statement) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }

  }

}
