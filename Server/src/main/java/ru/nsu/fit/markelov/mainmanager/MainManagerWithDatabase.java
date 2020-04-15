package ru.nsu.fit.markelov.mainmanager;

import java.sql.ResultSet;
import java.util.UUID;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Level.LevelDifficulty;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.Playback;
import ru.nsu.fit.markelov.interfaces.client.Resource;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.interfaces.client.User.UserType;
import ru.nsu.fit.markelov.interfaces.server.DatabaseHandler;
import ru.nsu.fit.markelov.interfaces.server.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.database.SQLiteDatabaseHandler;
import ru.nsu.fit.markelov.simulator.HardcodedSimulatorManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ru.nsu.fit.markelov.simulator.MissingSimulationUnits;

public class MainManagerWithDatabase implements MainManager {

  @Override
  public String getUserName(String token) {
    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    return user.getName();
  }

  @Override
  public UserType getUserType(String token) {
    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    return user.getType();
  }

  @Override
  public Map<Level, Iterable<SimulationResult>> getSolutions(String token, String userName) {
    Map<Level, Iterable<SimulationResult>> solutions = new HashMap<>();
    for (Level level : getLevels(token)) {
      solutions.put(level, getUserSimulationResultsOnLevel(getToken(token), userName, level.getId()));
    }
    return solutions;
  }

  @Override
  public String login(String userName, String password) {
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    if (!user.getPassword().equals(password)) {
      throw new ProcessingException("Incorrect password");
    }
    UUID uuid = UUID.randomUUID();
    tokenUserMap.put(uuid, user);
    return uuid.toString();
  }

  @Override
  public void logout(String token) {
    UUID id = getToken(token);
    if (getUserFromTokenMap(id) == null) {
      throw new ProcessingException("The user is not logged in");
    }
    tokenUserMap.remove(id);
  }

  @Override
  public String getLog(String token, String userName, int simulationResultId) {
    SimulationResultExtended result = simulationResultKeyMap.get(simulationResultId);
    if (result == null) {
      throw new ProcessingException("Simulation result not found");
    }

    return result.getLog(userName);
  }

  @Override
  public String getScript(String token, String userName, int simulationResultId) {
    return "null";
  }

  private DatabaseHandler databaseHandler;
  private SimulatorManager simulatorManager;
  private Map<String, UserExtended> userMap;
  private Map<Integer, LobbyExtended> lobbyMap;
  private Map<Integer, Level> levelMap;
  private Map<UserExtended, Map<Level, List<SimulationResultExtended>>> simulationResultMap;
  private Set<String> simulators;
  private Map<Integer, SimulationResultExtended> simulationResultKeyMap;
  private Map<UUID, UserExtended> tokenUserMap;
  private int currentLobbyId;
  private int currentLevelId;

  public MainManagerWithDatabase() {
    currentLobbyId = 0;
    currentLevelId = 0;
    databaseHandler = new SQLiteDatabaseHandler();
    simulatorManager = new HardcodedSimulatorManager();
    userMap = new HashMap<>();
    lobbyMap = new HashMap<>();
    levelMap = new HashMap<>();
    simulationResultKeyMap = new HashMap<>();
    simulationResultMap = new HashMap<>();
    tokenUserMap = new HashMap<>();

    if (databaseHandler.getUserByName("admin") == null) { // Temporary solution
      UserExtended admin = new User1("/images/person-icon.png", "admin", UserType.Admin, "admin");
      databaseHandler.saveUser(admin);
    }

    simulators = new HashSet<>(databaseHandler.getSimulatorsUrls());
    for (String simulator : simulators) {
      simulatorManager.addSimulator(simulator);
    }

    List<UserExtended> users = databaseHandler.getUsers();
    for (UserExtended user : users) {
      userMap.put(user.getName(), user);
      simulationResultMap.put(user, new HashMap<>());
    }

    List<Level> levels = databaseHandler.getLevels();
    for (Level level : levels) {
      levelMap.put(level.getId(), level);
      for (Map<Level, List<SimulationResultExtended>> map : simulationResultMap.values()) {
        map.put(level, new ArrayList<>());
      }
      if (level.getId() > currentLevelId) {
        currentLevelId = level.getId() + 1;
      }
    }


    /*List<SimulationResultExtended> simulationResults = databaseHandler.getSimulationResults();
    for (SimulationResultExtended result : simulationResults) {
      if (result.getId() >= currentLobbyId) {
        currentLobbyId = result.getId() + 1; // setting correct lobbyId
      }

      simulationResultKeyMap.put(result.getId(), result);

      Level level = getLevelFromMap(result.getLevelId());
      if (level == null) continue;

      for (String username : result.getUserNames()) {
        UserExtended user = getUserFromMap(username);
        if (user == null) continue;
        List<SimulationResultExtended> list = simulationResultMap.get(user).get(level);
        list.add(result);
      }
    }*/
  }

  @Override
  public Collection<Lobby> getLobbies(String token) {
    List<LobbyExtended> lobbies = new ArrayList<>(lobbyMap.values());
    lobbies.sort(Comparator.comparing(LobbyExtended::getCreationDate));
    Collections.reverse(lobbies);
    return new ArrayList<>(lobbies);
  }

  @Override
  public Collection<Level> getLevels(String token) {
    return levelMap.values();
  }

  @Override
  public Lobby joinLobby(String token, int lobbyID) {
    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    LobbyExtended lobby = getLobbyFromMap(lobbyID);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    if (lobby.getUsersWithoutPair().contains(user)) {
      return lobby;
    }

    if (lobby.getCurrentPlayersAmount() == lobby.getAcceptablePlayersAmount()) {
      throw new ProcessingException("The lobby is already full");
    }

    lobby.addPlayer(user);
    refreshActiveTime(user);

    return lobby;
  }

  @Override
  public Lobby createLobby(String token, int levelID, int playersAmount) {
    UserExtended host = getUserFromTokenMap(getToken(token));
    if (host == null) {
      throw new ProcessingException("User not found");
    }

    Level level = getLevelFromMap(levelID);
    if (level == null) {
      throw new ProcessingException("Level not found");
    }

    refreshActiveTime(host);

    LobbyExtended lobby = new Lobby1(currentLobbyId, level, playersAmount, host);

    lobbyMap.put(currentLobbyId, lobby);
    currentLobbyId++;

    return lobby;
  }

  @Override
  public void leaveLobby(String token, int lobbyID) {
    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    LobbyExtended lobby = getLobbyFromMap(lobbyID);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    lobby.removePlayer(user);

    refreshActiveTime(user);

    if (lobby.getCurrentPlayersAmount() == 0) {
      removeLobby(lobbyID);
    }
  }

  @Override
  public Lobby returnToLobby(String token, int lobbyID) {
    LobbyExtended lobby = getLobbyFromMap(lobbyID);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }
    UserExtended player = getUserFromTokenMap(getToken(token));
    if (player == null) {
      throw new ProcessingException("User not found");
    }
    if (lobby.getCode(player) != null) {
      lobby.setReady(player, true);
    }
    return lobby;
  }

  @Override
  public CompileResult submit(String token, int lobbyId, String code) {
    LobbyExtended lobby = getLobbyFromMap(lobbyId);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    if (!lobby.getUsersWithoutPair().contains(user)) {
      throw new ProcessingException("The user is not in the lobby");
    }

    Level level = lobby.getLevel();

    CompileResult compileResult = simulatorManager.checkCompilation(level.getLanguage(), code);
//    CompileResult compileResult = new CompileResult1("QWE", true, false);

    if (!compileResult.isCompiled()) {
      return compileResult;
    }

    lobby.submit(user, code);

    refreshActiveTime(user);

    if (lobby.isReady()) {
      compileResult = new CompileResult1(compileResult.getMessage(), true, true);
      SimulationResultExtended simulationResult;
      try {
        simulationResult = simulatorManager.runSimulation(level.getName(),
            lobbyId, new HashMap<>(lobby.getSolutionsMap()));
      } catch (MissingSimulationUnits e) {
        lobby.setReady(user, false);
        throw new ProcessingException(e.getMessage());
      }

      simulationResultKeyMap.put(lobbyId, simulationResult);
      databaseHandler.addSimulationResult(simulationResult);

      Map<String, UserExtended> usernameMap = new HashMap<>();
      for (UserExtended player : lobby.getUsersWithoutPair()) {
        usernameMap.put(player.getName(), player);
      }
      simulationResult.putUsersMap(usernameMap); // TODO kostyl for now

      for (UserExtended player : lobby.getUsersWithoutPair()) {
        List<SimulationResultExtended> list = simulationResultMap.get(player).get(level);
        if (list == null) continue;
        list.add(simulationResult);
      }

      removeLobby(lobbyId);
    }
    return compileResult;
  }

  @Override
  public String editSubmittedCode(String token, int lobbyId) {
    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    LobbyExtended lobby = getLobbyFromMap(lobbyId);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    refreshActiveTime(user);

    lobby.setReady(user, false);
    return lobby.getCode(user);
  }

  @Override
  public boolean isSimulationFinished(String token, int lobbyId) {
    return simulationResultKeyMap.get(lobbyId) != null;
  }

  @Override
  public SimulationResult getSimulationResult(String token, int lobbyId) {
    UserExtended user = getUserFromTokenMap(getToken(token));
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    return simulationResultKeyMap.get(lobbyId);
  }

  private List<SimulationResult> getUserSimulationResultsOnLevel(UUID token, String username,
      int levelId) {
    UserExtended user = getUserFromMap(username);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    Level level = getLevelFromMap(levelId);
    if (level == null) {
      throw new ProcessingException("Level not found");
    }

    List<SimulationResultExtended> results = simulationResultMap.get(user).get(level);

    return new ArrayList<>(results);
  }

  @Override
  public Collection<User> getUsers(String token) {
    UserExtended reference = getUserFromTokenMap(getToken(token));

    if (reference == null) {
      throw new ProcessingException("User not found");
    }

    if (reference.getType() == UserType.Student) {
      throw new ProcessingException("User is a Student");
    }

    List<UserExtended> list = new ArrayList<>(userMap.values());

    if (reference.getType() == UserType.Admin) {
      list.sort(Comparator.comparing(User::getName));
      return new ArrayList<>(list);
    }
    return list.stream().filter(u -> u.getType() == UserType.Student).collect(
        Collectors.toList());
  }

  @Override
  public void blockUser(String token, String userName, boolean block) {
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    user.setBlocked(block);
    databaseHandler.saveUser(user);
  }

  @Override
  public void removeUser(String token, String userName) {
    UserExtended user = getUserFromMap(userName);
    if (user != null) {
      userMap.remove(userName);
      simulationResultMap.remove(user);
      databaseHandler.removeUser(user);
    }
  }

  @Override
  public void submitLevel(String token, boolean create, Integer levelID, String name,
                          String difficulty, Integer minPlayers, Integer maxPlayers,
                          Resource iconResource, String description, String rules, String goal,
                          Collection<Resource> levelResources, String code, String language) {
    if (create) {
      int newLevelId = currentLevelId++;
      LevelDifficulty levelDifficulty;
      try {
        levelDifficulty = LevelDifficulty.valueOf(difficulty);
      } catch (IllegalArgumentException e) {
        throw new ProcessingException("Illegal difficulty");
      }
      Level level = new Level1(newLevelId, "/images/labyrinth-icon.png", name, levelDifficulty, "Type", description, rules, goal,
          minPlayers, maxPlayers, language, code); // ???


      List<Resource> resources;
      if (levelResources == null) {
        resources = new ArrayList<>();
      } else {
        resources = new ArrayList<>(levelResources);
      }

      simulatorManager.addLevel(Integer.toString(newLevelId), language, code, resources);

      databaseHandler.saveLevel(level);
      levelMap.put(newLevelId, level);
      for (Map<Level, List<SimulationResultExtended>> map : simulationResultMap.values()) {
        map.put(level, new ArrayList<>());
      }
    } else {
      Level level = getLevelFromMap(levelID);

      if (level == null) {
        throw new ProcessingException("Level does not exist");
      }

      LevelDifficulty levelDifficulty;
      try {
        levelDifficulty = LevelDifficulty.valueOf(difficulty);
      } catch (IllegalArgumentException e) {
        throw new ProcessingException("Illegal difficulty");
      }
      Level newLevel = new Level1(levelID, "/images/labyrinth-icon.png", name, levelDifficulty, "Type", description, rules, goal,
          minPlayers, maxPlayers, language, code); // ???

      List<Resource> resources; // TODO change to database interaction
      if (levelResources == null) {
        resources = new ArrayList<>();
      } else {
        resources = new ArrayList<>(levelResources);
      }

      simulatorManager.removeLevel(Integer.toString(levelID), language);
      simulatorManager.addLevel(Integer.toString(levelID), language, code, resources);

      databaseHandler.removeLevel(level);
      databaseHandler.saveLevel(newLevel);

      levelMap.put(levelID, newLevel);
      for (Map<Level, List<SimulationResultExtended>> map : simulationResultMap.values()) {
        map.put(level, new ArrayList<>());
      }
    }
  }

  @Override
  public Level getLevel(String token, int levelID) {
    Level level = getLevelFromMap(levelID);
    if (level == null) {
      throw new ProcessingException("Level not found");
    }
    return level;
  }

  @Override
  public void deleteLevel(String token, int levelID) {
    Level level = getLevelFromMap(levelID);

    databaseHandler.removeLevel(level);

    if (level == null) {
      throw new ProcessingException("Level not found");
    }

    simulatorManager.removeLevel(Integer.toString(level.getId()), level.getLanguage());

    levelMap.remove(levelID);
    for (Map<Level, List<SimulationResultExtended>> map : simulationResultMap.values()) {
      map.remove(level);
    }
  }

  @Override
  public Collection<String> getSimulators(String token) {
    return simulators;
  }

  @Override
  public void addSimulator(String token, String url) {
    simulatorManager.addSimulator(url);
    databaseHandler.saveSimulatorUrl(url);
    simulators.add(url);
  }

  @Override
  public void removeSimulator(String token, String url) {
    databaseHandler.removeSimulatorUrl(url);
    simulators.remove(url);
    simulatorManager.removeSimulator(url);
  }

  @Override
  public User getUser(String token, String userName) {
    User user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    return user;
  }

  @Override
  public void submitUser(String token, boolean create, String userName, String password, String type,
      Resource avatarResource) {

    UserType userType;
    try {
      userType = UserType.valueOf(type);
    } catch (IllegalArgumentException e) {
      throw new ProcessingException("Illegal user type");
    }

    if (create) {
      if (getUserFromMap(userName) != null) {
        throw new ProcessingException("User already exists");
      }

      UserExtended user = new User1("/images/person-icon.png", userName, userType, password);

      databaseHandler.saveUser(user);

      userMap.put(userName, user);

      Map<Level, List<SimulationResultExtended>> map = new HashMap<>();
      for (Level level : levelMap.values()) {
        map.put(level, new ArrayList<>());
      }
      simulationResultMap.put(user, map);

      return;
    }

    // update
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    user.setPassword(password);
    user.setType(userType);

    databaseHandler.saveUser(user);
  }


  private UserExtended getUserFromMap(String name) {
    return userMap.get(name);
  }

  private LobbyExtended getLobbyFromMap(int id) {
    return lobbyMap.get(id);
  }

  private Level getLevelFromMap(int id) {
    return levelMap.get(id);
  }

  private void refreshActiveTime(UserExtended user) {
    user.refreshLastActive();
    databaseHandler.saveUser(user);
  }

  private UserExtended getUserFromTokenMap(UUID token) {
    return tokenUserMap.get(token);
  }

  private UUID getToken(String token) {
    if (token == null) {
      throw new ProcessingException("Token is null");
    }
    return UUID.fromString(token);
  }

  private void removeLobby(int id) {
    lobbyMap.remove(id);
  }
}

