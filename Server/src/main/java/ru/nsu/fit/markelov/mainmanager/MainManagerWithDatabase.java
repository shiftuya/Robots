package ru.nsu.fit.markelov.mainmanager;

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

public class MainManagerWithDatabase implements MainManager {
  private DatabaseHandler databaseHandler;
  private SimulatorManager simulatorManager;
  private Map<String, UserExtended> userMap;
  private Map<Integer, LobbyExtended> lobbyMap;
  private Map<Integer, Level> levelMap;
  private Map<UserExtended, Map<Level, List<SimulationResultExtended>>> simulationResultMap;
  private Set<String> simulators;
  private Map<Integer, SimulationResultExtended> simulationResultKeyMap;

  private int currentLobbyId;

  public MainManagerWithDatabase() {
    currentLobbyId = 0;
    databaseHandler = new SQLiteDatabaseHandler();
    simulatorManager = new HardcodedSimulatorManager();
    userMap = new HashMap<>();
    lobbyMap = new HashMap<>();
    levelMap = new HashMap<>();
    simulationResultKeyMap = new HashMap<>();
    simulationResultMap = new HashMap<>();

    simulators = new HashSet<>(databaseHandler.getSimulatorsUrls());

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
    }

    List<SimulationResultExtended> simulationResults = databaseHandler.getSimulationResults();
    for (SimulationResultExtended result : simulationResults) {
      if (result.getId() >= currentLobbyId) {
        currentLobbyId = result.getId() + 1; // setting correct lobbyId
      }

      simulationResultKeyMap.put(result.getId(), result);

      Level level = getLevelFromMap(result.getLevelId());
      if (level == null) continue;

      for (String username : result.getUsers()) {
        UserExtended user = getUserFromMap(username);
        if (user == null) continue;
        List<SimulationResultExtended> list = simulationResultMap.get(user).get(level);
        list.add(result);
      }
    }
  }

  @Override
  public boolean login(String username) {
    return false;
  }

  @Override
  public boolean logout(String username) {
    return false;
  }

  @Override
  public Collection<Lobby> getLobbies() { // ok
    List<LobbyExtended> lobbies = new ArrayList<>(lobbyMap.values());
    lobbies.sort(Comparator.comparing(LobbyExtended::getCreationDate));
    Collections.reverse(lobbies);
    return new ArrayList<>(lobbies);
  }

  @Override
  public Collection<Level> getLevels() { // ok
    return levelMap.values();
  }

  @Override
  public Lobby joinLobby(String userName, int lobbyID) { // ok
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    LobbyExtended lobby = getLobbyFromMap(lobbyID);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    if (lobby.getUsersWithoutPair().contains(user)) {
      throw new ProcessingException("The lobby already contains the user");
    }

    if (lobby.getCurrentPlayersAmount() == lobby.getAcceptablePlayersAmount()) {
      throw new ProcessingException("The lobby is already full");
    }

    lobby.addPlayer(user);
    refreshActiveTime(user);

    return lobby;
  }

  @Override
  public Lobby createLobby(String userName, int levelID, int playersAmount) { // ok
    UserExtended host = getUserFromMap(userName);
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
  public boolean leaveLobby(String userName, int lobbyID) {
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    LobbyExtended lobby = getLobbyFromMap(lobbyID);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    lobby.removePlayer(user);

    refreshActiveTime(user);

    return true;
  }

  @Override
  public Lobby returnToLobby(String userName, int lobbyID) {
    return null;
  }

  @Override
  public CompileResult submit(String username, int lobbyId, String code) {
    LobbyExtended lobby = getLobbyFromMap(lobbyId);
    if (lobby == null) {
      throw new ProcessingException("Lobby not found");
    }

    UserExtended user = getUserFromMap(username);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    if (!lobby.getUsersWithoutPair().contains(user)) {
      throw new ProcessingException("The user is not in the lobby");
    }

    lobby.submit(user, code);

    Level level = lobby.getLevel();

    refreshActiveTime(user);

    boolean isSimulated = false;
    if (lobby.isReady()) {
      isSimulated = true;
      // simulatorManager.runSimulation() How to run it?
      // The following is a temporary placeholder
      Map<String, Playback> playbackMap = new HashMap<>();
      Map<String, String> logMap = new HashMap<>();
      Map<String, Boolean> successMap = new HashMap<>();
      for (UserExtended player : lobby.getUsersWithoutPair()) {
        playbackMap.put(player.getName(), null);
        logMap.put(player.getName(), "heh");
        successMap.put(player.getName(), true);
      }
      SimulationResultExtended simulationResult = new SimulationResult1(lobbyId, successMap,
          new Date(), logMap, playbackMap, lobby.getLevel().getId());

      simulationResultKeyMap.put(lobbyId, simulationResult);
      databaseHandler.addSimulationResult(simulationResult);

      for (UserExtended player : lobby.getUsersWithoutPair()) {
        List<SimulationResultExtended> list = simulationResultMap.get(player).get(level);
        if (list == null) continue;
        list.add(simulationResult);
      }
    }

    return new CompileResult1("Compiled", true, isSimulated);
  }

  @Override
  public String editSubmittedCode(String username, int lobbyId) {
    UserExtended user = getUserFromMap(username);
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
  public boolean isSimulationFinished(int lobbyId) {
    return simulationResultKeyMap.get(lobbyId) != null;
  }

  @Override
  public SimulationResult getSimulationResult(String username, int lobbyId) {
    UserExtended user = getUserFromMap(username);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    return simulationResultKeyMap.get(lobbyId);
  }

  @Override
  public Collection<SimulationResult> getUserSimulationResultsOnLevel(String username,
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
  public Collection<User> getUsers(String userName) {
    UserExtended reference = getUserFromMap(userName);

    if (reference == null) {
      throw new ProcessingException("User not found");
    }

    if (reference.getType() == UserType.Student) {
      throw new ProcessingException("User " + userName + " is a Student");
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
  public boolean blockUser(String userName, boolean block) {
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    user.setBlocked(block);
    databaseHandler.saveUser(user);
    return true;
  }

  @Override
  public boolean removeUser(String userName) {
    UserExtended user = getUserFromMap(userName);
    if (user != null) {
      userMap.remove(userName);
      simulationResultMap.remove(user);
      databaseHandler.removeUser(user);
    }
    return true;
  }

  @Override
  public boolean submitLevel(Integer levelID, String name, String difficulty, Integer minPlayers,
      Integer maxPlayers, Resource iconResource, String description, String rules, String goal,
      Collection<Resource> levelResources, String code, String language) {
    if (getLevelFromMap(levelID) != null) {
      throw new ProcessingException("Level already exists");
    }

    LevelDifficulty levelDifficulty;
    try {
      levelDifficulty = LevelDifficulty.valueOf(difficulty);
    } catch (IllegalArgumentException e) {
      throw new ProcessingException("Illegal difficulty");
    }
    Level level = new Level1(levelID, "/images/labyrinth-icon.png", name, levelDifficulty, "heh", description, rules, goal,
        minPlayers, maxPlayers, language, code); // ???

    databaseHandler.saveLevel(level);
    levelMap.put(levelID, level);
    for (Map<Level, List<SimulationResultExtended>> map : simulationResultMap.values()) {
      map.put(level, new ArrayList<>());
    }

    return true;
  }

  @Override
  public Level getLevel(int levelID) {
    Level level = getLevelFromMap(levelID);
    if (level == null) {
      throw new ProcessingException("Level not found");
    }
    return level;
  }

  @Override
  public boolean deleteLevel(int levelID) {
    Level level = getLevelFromMap(levelID);

    databaseHandler.removeLevel(level);

    if (level == null) {
      return true;
    }

    levelMap.remove(levelID);
    for (Map<Level, List<SimulationResultExtended>> map : simulationResultMap.values()) {
      map.remove(level);
    }

    return true;
  }

  @Override
  public Collection<String> getSimulators() {
    return simulators;
  }

  @Override
  public boolean addSimulator(String url) {
    databaseHandler.saveSimulatorUrl(url);
    simulators.add(url);
    return true;
  }

  @Override
  public boolean removeSimulator(String url) {
    databaseHandler.removeSimulatorUrl(url);
    simulators.remove(url);
    return true;
  }

  @Override
  public User getUser(String userName) {
    User user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }
    return user;
  }

  @Override
  public boolean submitUser(boolean create, String userName, String password, String type,
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

      return true;
    }

    // update
    UserExtended user = getUserFromMap(userName);
    if (user == null) {
      throw new ProcessingException("User not found");
    }

    user.setPassword(password);
    user.setType(userType);

    databaseHandler.saveUser(user);

    return true;
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
}
