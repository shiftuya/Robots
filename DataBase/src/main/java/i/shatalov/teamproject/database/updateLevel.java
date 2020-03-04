package i.shatalov.teamproject.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

public class updateLevel {
  // JDBC driver name and database URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/robots";

  //  Database credentials
  static final String USER = "root";
  static final String PASS = "Pai4Piqwerty";

  public void saveLevel(Level level) throws SQLException {
    Connection conn = null;
    Statement stmt = null;
    try {
      //Register JDBC driver
      Class.forName(JDBC_DRIVER);
      //Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully.");

      InputStream inputStream = new FileInputStream(new File(level.path));
      String sql = "INSERT INTO levels " +
          "(file, description, difficulty, playersMax) values (?, ?, ?, ?)";
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setBlob(1, inputStream);
      statement.setString(2, level.description);
      statement.setString(3, level.description);
      statement.setInt(4, level.playersMax);
      statement.executeUpdate();

    } catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    finally {
      conn.close();
    }
  }
}
