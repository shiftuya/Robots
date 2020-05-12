package ru.nsu.fit.markelov.mainmanager.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Level.LevelDifficulty;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.interfaces.client.User.UserType;
import ru.nsu.fit.markelov.interfaces.server.DatabaseHandler;
import ru.nsu.fit.markelov.mainmanager.Level1;
import ru.nsu.fit.markelov.mainmanager.SimulationResult1;
import ru.nsu.fit.markelov.mainmanager.SimulationResultExtended;
import ru.nsu.fit.markelov.mainmanager.User1;
import ru.nsu.fit.markelov.mainmanager.UserExtended;

public class SQLiteDatabaseHandler implements DatabaseHandler {
  private Connection connection;
  private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/database.db";

  private static final String SIMULATORS_TABLE = "simulators";
  private static final String SIMULATORS_URL = "url";

  private static final String USERS_TABLE = "users";
  private static final String USERS_NAME = "username";
  private static final String USERS_AVATAR = "avatar";
  private static final String USERS_TYPE = "type";
  private static final String USERS_LAST_ACCESS = "date";
  private static final String USERS_PASSWORD = "password";
  private static final String USERS_BLOCKED = "blocked";

  private static final String RESULTS_TABLE = "results";
  private static final String RESULTS_KEY = "id";
  private static final String RESULTS_PLAYER = "resultPlayer";
  private static final String RESULTS_SUCCESS = "success";
  private static final String RESULTS_DATE = "resultDate";
  private static final String RESULTS_LOG = "code";
  private static final String RESULTS_PLAYBACK = "playback";
  private static final String RESULTS_LEVEL = "levelIdOfResult";

  private static final String LEVELS_TABLE = "levels";
  private static final String LEVELS_ID = "levelId";
  private static final String LEVELS_ICON = "iconAddress";
  private static final String LEVELS_NAME = "levelName";
  private static final String LEVELS_DIFFICULTY = "levelDifficulty";
  private static final String LEVELS_TYPE = "levelType";
  private static final String LEVELS_DESCRIPTION = "levelDescription";
  private static final String LEVELS_RULES = "levelRules";
  private static final String LEVELS_GOAL = "levelGoal";
  private static final String LEVELS_MIN_PLAYERS = "levelMinPlayers";
  private static final String LEVELS_MAX_PLAYERS = "levelMaxPlayers";
  private static final String LEVELS_CODE = "levelCode";
  private static final String LEVELS_LANGUAGE = "levelLang";

  private void connect() {
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection(DB_URL);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void disconnect() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public SQLiteDatabaseHandler() {
    String simulatorSql = String.format("CREATE TABLE IF NOT EXISTS %s "
        + "(%s TEXT PRIMARY KEY)", SIMULATORS_TABLE, SIMULATORS_URL);

    String usersSql = String.format("CREATE TABLE IF NOT EXISTS %s "
        + "(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s INTEGER, "
        + "%s INTEGER, %s TEXT)", USERS_TABLE, USERS_NAME, USERS_AVATAR,
        USERS_TYPE, USERS_LAST_ACCESS, USERS_BLOCKED, USERS_PASSWORD);

    String resultsSql = String.format("CREATE TABLE IF NOT EXISTS %s "
        + "(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, %s TEXT, %s BLOB, %s INTEGER, %s INTEGER)",
        RESULTS_TABLE, RESULTS_KEY, RESULTS_PLAYER, RESULTS_DATE, RESULTS_LOG,
        RESULTS_PLAYBACK, RESULTS_SUCCESS, RESULTS_LEVEL);

    String levelsSql = String.format("CREATE TABLE IF NOT EXISTS %s "
        + "(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, "
        + "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, "
        + "%s INTEGER, %s INTEGER, %s TEXT, %s TEXT)",
        LEVELS_TABLE, LEVELS_ID, LEVELS_CODE, LEVELS_NAME, LEVELS_DESCRIPTION,
        LEVELS_DIFFICULTY, LEVELS_GOAL, LEVELS_ICON, LEVELS_LANGUAGE,
        LEVELS_MIN_PLAYERS, LEVELS_MAX_PLAYERS, LEVELS_TYPE, LEVELS_RULES);

    connect();
    try (Statement statement = connection.createStatement()) {
      statement.execute(simulatorSql);
      statement.execute(usersSql);
      statement.execute(resultsSql);
      statement.execute(levelsSql);
    } catch (SQLException e) {
      throw new ProcessingException("Unable to create or open database");
    } finally {
      disconnect();
    }
  }

  @Override
  public List<UserExtended> getUsers() {
    String sql = "SELECT * FROM " + USERS_TABLE;
    List<UserExtended> users = new ArrayList<>();

    connect();
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        users.add(new User1(resultSet.getString(USERS_AVATAR),
            resultSet.getString(USERS_NAME),
            UserType.valueOf(resultSet.getString(USERS_TYPE)),
            resultSet.getBoolean(USERS_BLOCKED),
            resultSet.getDate(USERS_LAST_ACCESS),
            resultSet.getString(USERS_PASSWORD)));
      }
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to get a users list");
    } finally {
      disconnect();
    }

    return users;
  }

  @Override
  public UserExtended getUserByName(String name) {
    String sql = "SELECT * FROM users WHERE username = ?";
    connect();
    UserExtended user = null;
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        user = new User1(resultSet.getString("avatar"),
            name, User.UserType.valueOf(resultSet.getString("type")),
            resultSet.getBoolean("blocked"),
            resultSet.getDate("date"),
            resultSet.getString("password"));
      }
    } catch (SQLException e) {
      throw new ProcessingException("heh");
    } finally {
      disconnect();
    }
    return user;
  }

  @Override
  public void saveUser(UserExtended user) {
    String sql = "REPLACE INTO users (username, avatar, type, date, blocked, password) "
        + "VALUES (?, ?, ?, ?, ?, ?)";
    connect();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, user.getName());
      statement.setString(2, user.getAvatarAddress());
      statement.setString(3, user.getType().toString());
      statement.setDate(4, new Date(user.getLastActive().getTime()));
      statement.setBoolean(5, user.isBlocked());
      statement.setString(6, user.getPassword());
      statement.execute();
    } catch (SQLException e) {
      throw new ProcessingException("Database exception");
    } finally {
      disconnect();
    }
  }

  @Override
  public void removeUser(UserExtended user) {
    String sql = String.format("DELETE FROM %s WHERE %s = ?", USERS_TABLE, USERS_NAME);
    connect();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, user.getName());
      statement.execute();
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to delete a user");
    } finally {
      disconnect();
    }
  }

  @Override
  public List<String> getSimulatorsUrls() {
    String sql = "SELECT * FROM " + SIMULATORS_TABLE;
    List<String> urls = new ArrayList<>();
    connect();
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        urls.add(resultSet.getString(SIMULATORS_URL));
      }
    } catch (SQLException e) {
      throw new ProcessingException("Exception while getting simulators list");
    } finally {
      disconnect();
    }
    return urls;
  }

  @Override
  public void removeSimulatorUrl(String url) {
    String sql = String.format("DELETE FROM %s WHERE %s = ?", SIMULATORS_TABLE, SIMULATORS_URL);
    connect();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, url);
      statement.execute();
    } catch (SQLException e) {
      throw new ProcessingException("Exception while removing a simulator");
    } finally {
      disconnect();
    }
  }

  @Override
  public void saveSimulatorUrl(String url) {
    String sql = String.format("REPLACE INTO %s (%s) VALUES (?)", SIMULATORS_TABLE, SIMULATORS_URL);
    connect();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, url);
      statement.execute();
    } catch (SQLException e) {
      throw new ProcessingException("Exception while saving a simulator");
    } finally {
      disconnect();
    }
  }

  @Override
  public void saveLevel(Level level) {
    String sql = String.format("REPLACE INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", LEVELS_TABLE,
        LEVELS_ID, LEVELS_NAME, LEVELS_RULES, LEVELS_MIN_PLAYERS, LEVELS_MAX_PLAYERS,
        LEVELS_DIFFICULTY, LEVELS_DESCRIPTION, LEVELS_GOAL, LEVELS_CODE, LEVELS_ICON,
        LEVELS_LANGUAGE, LEVELS_TYPE);
    connect();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, level.getId());
      statement.setString(2, level.getName());
      statement.setString(3, level.getRules());
      statement.setInt(4, level.getMinPlayers());
      statement.setInt(5, level.getMaxPlayers());
      statement.setString(6, level.getDifficulty().toString());
      statement.setString(7, level.getDescription());
      statement.setString(8, level.getGoal());
      statement.setString(9, level.getCode());
      statement.setString(10, level.getIconAddress());
      statement.setString(11, level.getLanguage());
      statement.setString(12, level.getType());
      statement.execute();
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to save a level");
    } finally {
      disconnect();
    }
  }

  @Override
  public void removeLevel(Level level) {
    String sql = String.format("DELETE FROM %s WHERE %s = ?", LEVELS_TABLE, LEVELS_ID);
    connect();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, level.getId());
      statement.execute();
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to remove a level");
    } finally {
      disconnect();
    }
  }

  @Override
  public List<Level> getLevels() {
    String sql = String.format("SELECT * FROM %s", LEVELS_TABLE);
    List<Level> list = new ArrayList<>();
    connect();
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        list.add(new Level1(resultSet.getInt(LEVELS_ID),
            resultSet.getString(LEVELS_ICON),
            resultSet.getString(LEVELS_NAME),
            LevelDifficulty.valueOf(resultSet.getString(LEVELS_DIFFICULTY)),
            resultSet.getString(LEVELS_TYPE),
            resultSet.getString(LEVELS_DESCRIPTION),
            resultSet.getString(LEVELS_RULES),
            resultSet.getString(LEVELS_GOAL),
            resultSet.getInt(LEVELS_MIN_PLAYERS),
            resultSet.getInt(LEVELS_MAX_PLAYERS),
            resultSet.getString(LEVELS_LANGUAGE),
            resultSet.getString(LEVELS_CODE)));
      }
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to get a levels list");
    } finally {
      disconnect();
    }
    return list;
  }

  @Override
  public Level getLevelById(int id) {
    String sql = String.format("SELECT * FROM %s WHERE %s = ?", LEVELS_TABLE, LEVELS_ID);
    connect();
    Level level = null;
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        level = new Level1(resultSet.getInt(LEVELS_ID),
            resultSet.getString(LEVELS_ICON),
            resultSet.getString(LEVELS_NAME),
            LevelDifficulty.valueOf(resultSet.getString(LEVELS_DIFFICULTY)),
            resultSet.getString(LEVELS_TYPE),
            resultSet.getString(LEVELS_DESCRIPTION),
            resultSet.getString(LEVELS_RULES),
            resultSet.getString(LEVELS_GOAL),
            resultSet.getInt(LEVELS_MIN_PLAYERS),
            resultSet.getInt(LEVELS_MAX_PLAYERS),
            resultSet.getString(LEVELS_LANGUAGE),
            resultSet.getString(LEVELS_CODE));
      }
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to get a level");
    } finally {
      disconnect();
    }
    return level;
  }

  @Override
  public void addSimulationResult(SimulationResultExtended result) {
    String sql = String.format("REPLACE INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
        RESULTS_TABLE, RESULTS_KEY, RESULTS_PLAYER, RESULTS_SUCCESS, RESULTS_PLAYBACK, RESULTS_LOG, RESULTS_DATE, RESULTS_LEVEL);
    connect();
    try {
      for (String player : result.getUserNames()) {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, result.getId());
        statement.setString(2, player);
        statement.setBoolean(3, result.isSuccessful(player));
        statement.setObject(4, result.getPlayback()); // Temporary solution! (kostil)
        statement.setString(5, /*TODO result.getLog(player)*/"Sorry...");
        statement.setDate(6, new Date(result.getDate().getTime()));
        statement.setInt(7, result.getLevelId());
        statement.execute();
      }
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to add a simulation result");
    } finally {
      disconnect();
    }
  }

  @Override
  public List<SimulationResultExtended> getSimulationResults() {
    String keySql = String.format("SELECT DISTINCT %s FROM %s", RESULTS_KEY, RESULTS_TABLE);
    String sql = String.format("SELECT * FROM %s WHERE %s = ?", RESULTS_TABLE, RESULTS_KEY);
    List<SimulationResultExtended> list = new ArrayList<>();
    connect();
    try (Statement keyStatement = connection.createStatement()) {
      ResultSet keysResultSet = keyStatement.executeQuery(keySql);

      while (keysResultSet.next()) {
        int key = keysResultSet.getInt(RESULTS_KEY);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, key);
        ResultSet resultSet = statement.executeQuery();

        Map<String, Boolean> successMap = new HashMap<>();
        Map<String, String> privateLogs = new HashMap<>();
        Map<String, Playback> playbackMap = new HashMap<>();
        while (resultSet.next()) {
          String player = resultSet.getString(RESULTS_PLAYER);
          successMap.put(player, resultSet.getBoolean(RESULTS_SUCCESS));
          privateLogs.put(player, resultSet.getString(RESULTS_LOG));
          playbackMap.put(player, (Playback) resultSet.getObject(RESULTS_PLAYBACK));
        }

        list.add(new SimulationResult1(key, successMap, resultSet.getDate(RESULTS_DATE),
            privateLogs, null, resultSet.getInt(RESULTS_LEVEL)));
      }
    } catch (SQLException e) {
      throw new ProcessingException("Exception while trying to get simulation results list");
    } finally {
      disconnect();
    }
    return list;
  }
}
