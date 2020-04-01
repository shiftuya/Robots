package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.Player;
import ru.nsu.fit.markelov.interfaces.client.Resource;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.server.SimulatorManager;
import ru.nsu.fit.markelov.simulator.HardcodedSimulatorManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainManager1 implements MainManager {
  @Override
  public boolean login(String userName) {

    if (getPlayerByName(userName) != null) {
      System.out.println(userName + " has already logged in!");
      return false;
    }

    addNewPlayer(userName, "/images/person-icon.png");
    System.out.println(userName + " logged in.");
    return true;
  }

  @Override
  public boolean logout(String userName) {
    Player1 player = getPlayerByName(userName);
    if (player == null) {
      System.out.println(userName + " hasn't logged in yet!");

      return false;
    }

    removePlayer(userName);
    System.out.println(userName + " logged out.");
    return true;
  }
  private List<Lobby> lobbies;

  private SimulatorManager simulatorManager;

  private int maxLobbyId, maxLevelId;

 // private Map<Integer, String> levelIdToFile;

  private Map<String, Player1> playerMap;

  private Map<Integer, Level1> idLevelMap;
  private Map<Integer, Lobby1> idLobbyMap;

  private Map<Integer, SimulationResult> simResultMap;

  private Map<Player, Map<Level, List<SimulationResult>>> playerSimResultMap;

  private List<Level> levels;

  // for my convenience - likely to go to another class
  private Player1 getPlayerByName(String name) {
    return playerMap.get(name);
  }

  private Level getLevelById(int id) {
    return idLevelMap.get(id);
  }

  private Lobby1 getLobbyById(int id) {
    return idLobbyMap.get(id);
  }

  private SimulationResult getSimulationResultByLobbyId(int id) {
    return simResultMap.get(id);
  }

  public MainManager1() {
    lobbies = new ArrayList<>();
    levels = new ArrayList<>();
    playerMap = new HashMap<>();
    maxLevelId = maxLobbyId = 0;
    simResultMap = new HashMap<>();
    idLobbyMap = new HashMap<>();
    idLevelMap = new HashMap<>();
    simulatorManager = new HardcodedSimulatorManager();

    playerSimResultMap = new HashMap<>();

    // Hard Code
    addNewLevel(++maxLevelId,
        "/images/labyrinth-icon.png",
        "Simple Plane",
        Level.LevelDifficulty.Easy,
        "Multiplayer",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim",
        "Suspendisse sed nisi lacus sed viverra tellus in hac habitasse",
        "Uscipit adipiscing bibendum est ultricies integer quis auctor elit sed",
        1, 10,
        "simple_plane");

    simulatorManager.addSimulator("http://localhost:1337");
  }

  // For hardcoding purposes
  Player addNewPlayer(String name, String avatarAddress) {
    Player1 player = new Player1(avatarAddress, name);
    playerMap.put(name, player);

    playerSimResultMap.put(player, new HashMap<>());
    return player;
  }

  void removePlayer(String name) {
    playerMap.remove(name);
  }

  // For hardcoding purposes
  void addNewLevel(int id, String iconAddress, String name, Level.LevelDifficulty difficulty, String type,
                   String description, String rules, String goal, int minPlayers, int maxPlayers, String filename) {

    Level1 level = new Level1(id, iconAddress, name, difficulty, type,
        description, rules, goal, minPlayers, maxPlayers, filename);

    idLevelMap.put(id, level);

    levels.add(level);
   // levelIdToFile.put(maxLevelId, filename);
  }

  @Override
  public boolean isSimulationFinished(int lobbyId) {
    return simResultMap.containsKey(lobbyId);
  }

  @Override
  public List<Lobby> getLobbies() {
    return lobbies;
  }

  @Override
  public List<Level> getLevels() {
    return levels;
  }

/*  @Override
  public List<Solution> getSolutions(String userName) {
    return null;
  }*/

  @Override
  public Lobby joinLobby(String userName, int lobbyID) {
    Lobby1 lobby = getLobbyById(lobbyID);
    Player player = getPlayerByName(userName);
    if (lobby.getPlayers().contains(player)) {
      return lobby;
    }
    if(lobby.getCurrentPlayersAmount() == lobby.getAcceptablePlayersAmount()) {
      return null;
    }
    lobby.addPlayer(player);
    return lobby;
  }

  @Override
  public Lobby createLobby(String userName, int levelID, int playersAmount) {
    Level level = getLevelById(levelID);
    Player host = getPlayerByName(userName);

    if (level == null || host == null) {
      return null;
    }

    int lobbyId = ++maxLobbyId;
    Lobby1 lobby = new Lobby1(lobbyId, level, playersAmount);
    lobby.addPlayer(host);

    idLobbyMap.put(lobbyId, lobby);
    lobbies.add(lobby);

    return lobby;
  }

  @Override
  public boolean leaveLobby(String userName, int lobbyID) {
    Player player = getPlayerByName(userName);
    Lobby1 lobby = getLobbyById(lobbyID);

    if (player == null || lobby == null) {
      return false;
    }

    boolean successfulLeave = lobby.removePlayer(player);
    if (lobby.getCurrentPlayersAmount() == 0) {
      lobbies.remove(lobby);
      idLobbyMap.remove(lobbyID);
    }

    return successfulLeave;
  }

  @Override
  public Lobby returnToLobby(String userName, int lobbyID) {
    Lobby1 lobby = getLobbyById(lobbyID);
    Player1 player = getPlayerByName(userName);
    if (!player.isSubmitted() && lobby.getSolutions().get(player) != null) {
      player.setSubmitted(true);
    }
    return lobby;
  }

  @Override
  public CompileResult submit(String username, int lobbyId, String code) {
    // TODO try to compile

    Lobby1 lobby = getLobbyById(lobbyId);
    Player1 player = getPlayerByName(username);
    lobby.addSolution(player, code);

    boolean isSimulated = lobby.isReady();

    if (lobby.isReady()) {
      simulateLobby(lobby);
    }

    return new CompileResult1("Compiled", true, isSimulated);
  }

  @Override
  public String editSubmittedCode(String username, int lobbyId) {
    Player1 player = getPlayerByName(username);
    Lobby1 lobby = getLobbyById(lobbyId);
    String code = lobby.getSolution(player);

    player.setSubmitted(false);

   // lobby.removeSolution(player);
    return code;
  }

  @Override
  public SimulationResult getSimulationResult(String username, int lobbyId) {
    return getSimulationResultByLobbyId(lobbyId);
    // LobbyId is not yet needed
  }

  private void simulateLobby(Lobby1 lobby) {
    SimulationResult result = simulatorManager
        .runSimulation(idLevelMap.get(lobby.getLevel().getId()).getFilename(),
            lobby.getId(), lobby.getSolutions());

    simResultMap.put(lobby.getId(), result);

    // Filling the Solutions Lists for each player
    for(Player player : lobby.getPlayers()) {
      ((Player1)player).setSubmitted(false);

      Map<Level, List<SimulationResult>> map = playerSimResultMap.get(player);
      map.computeIfAbsent(lobby.getLevel(), k -> new LinkedList<>());
      map.get(lobby.getLevel()).add(result);
    }

    lobbies.remove(lobby);
    idLobbyMap.remove(lobby.getId());
  }

  @Override
  public List<SimulationResult> getUserSimulationResultsOnLevel(String username, int levelId) {
    Player player = getPlayerByName(username);
    if (player == null) return null;

    Level level = getLevelById(levelId);
    if (level == null) return null;

    Map<Level, List<SimulationResult>> map = playerSimResultMap.get(player);
    map.computeIfAbsent(level, k -> new LinkedList<>());

    return new LinkedList<>(map.get(level));
  }

  @Override
  public boolean submitLevel(Integer levelID, String name, String difficulty, Integer minPlayers,
                             Integer maxPlayers, Resource iconResource, String description, String rules,
                             String goal, Collection<Resource> levelResources, String code, String language) {
    return false; // TODO
  }

  @Override
  public boolean deleteLevel(int levelID) {
    return false; // TODO
  }

  @Override
  public List<String> getSimulators() {
    return null;
  }

  @Override
  public boolean addSimulator(String url) {
    return false;
  }

  @Override
  public boolean removeSimulator(String url) {
    return false;
  }

  @Override
  public Level getLevel(int levelID) {
    return null;
  }
}
