package ru.nsu.fit.markelov.mainmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.CompileResult;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Lobby;
import ru.nsu.fit.markelov.interfaces.MainManager;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.SimulatorManager;
import ru.nsu.fit.markelov.interfaces.Solution;

public class MainManager1 implements MainManager {
  private List<Player> players;
  private List<Lobby> lobbies;

  private SimulatorManager simulatorManager;

  private int maxLobbyId, maxLevelId;

  private Map<Player, List<Solution>> playerSolutionsMap;

  private Map<Integer, String> levelIdToFile;

  private Map<String, Player1> playerMap;

  private Map<Integer, Level> idLevelMap;
  private Map<Integer, Lobby1> idLobbyMap;

  private Map<Integer, SimulationResult> simResultMap;

  private List<Level> levels;

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
    players = new ArrayList<>();
    lobbies = new ArrayList<>();
    levels = new ArrayList<>();
    playerSolutionsMap = new HashMap<>();
    playerMap = new HashMap<>();
    maxLevelId = maxLobbyId = 0;
    simResultMap = new HashMap<>();
    idLobbyMap = new HashMap<>();
    idLevelMap = new HashMap<>();
    levelIdToFile = new HashMap<>();
    // Hard Code
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
  public List<Lobby> getLobbies() {
    return lobbies;
  }

  @Override
  public List<Level> getLevels() {
    return levels;
  }

  @Override
  public List<Solution> getSolutions(String userName) {
    return playerSolutionsMap.get(getPlayerByName(userName));
  }

  @Override
  public Lobby joinLobby(String userName, int lobbyID) {
    Lobby1 lobby = getLobbyById(lobbyID);
    Player player = getPlayerByName(userName);
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

    return lobby;
  }

  @Override
  public boolean leaveLobby(String userName, int lobbyID) {
    Player player = getPlayerByName(userName);
    Lobby1 lobby = getLobbyById(lobbyID);

    if (player == null || lobby == null) {
      return false;
    }

    return lobby.removePlayer(player);
  }

  @Override
  public Lobby getLobby(String userName, int lobbyID) {
    return getLobbyById(lobbyID);
  }

  @Override
  public CompileResult submit(String username, String code, int lobbyId) {
    // TODO try to compile

    Lobby1 lobby = getLobbyById(lobbyId);
    Player1 player = getPlayerByName(username);
    lobby.addSolution(player, code);

    if (lobby.getCurrentPlayersAmount() == lobby.getAcceptablePlayersAmount()
        && lobby.getCurrentPlayersAmount() == lobby.getSolutions().size()) {
      simulateLobby(lobby);
    }

    return new CompileResult1("Compiled", true);
  }

  @Override
  public String editSubmittedCode(String username, int lobbyId) {
    Player player = getPlayerByName(username);
    Lobby1 lobby = getLobbyById(lobbyId);
    String code = lobby.getSolution(player);
    lobby.removeSolution(player);
    return code;
  }

  @Override
  public SimulationResult getSimulationResult(String username, int lobbyId) {
    return getSimulationResultByLobbyId(lobbyId);
    // LobbyId is not yet needed
  }

  private void simulateLobby(Lobby1 lobby) {

    SimulationResult result =
        simulatorManager.runSimulation(
            levelIdToFile.get(lobby.getLevel().getId()), lobby.getId(), lobby.getSolutions());
    // TODO replace with new structure
    // simResultMap.put(lobby.getId(), results);
    lobbies.remove(lobby);
    idLobbyMap.remove(lobby.getId());
  }
}
