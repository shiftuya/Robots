package ru.nsu.fit.markelov.mainmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.CompileResult;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Lobby;
import ru.nsu.fit.markelov.interfaces.MainManager;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.Solution;

public class MainManager1 implements MainManager {
  private List<Player> players;
  private List<Lobby> lobbies;

  private int maxLobbyId, maxLevelId;

  private Map<Player, List<Solution>> playerSolutionsMap;

  private Map<String, Player> playerMap;

  private Map<Integer, Level> idLevelMap;
  private Map<Integer, Lobby1> idLobbyMap;

  private List<Level> levels;

  private Player getPlayerByName(String name) {
    return playerMap.get(name);
  }

  private Level getLevelById(int id) {
    return idLevelMap.get(id);
  }

  private Lobby1 getLobbyById(int id) {
    return idLobbyMap.get(id);
  }

  public MainManager1() {
    players = new ArrayList<>();
    lobbies = new ArrayList<>();
    levels = new ArrayList<>();
    playerSolutionsMap = new HashMap<>();
    playerMap = new HashMap<>();
    maxLevelId = maxLobbyId = 0;
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
    return null;
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
    return null;
  }

  @Override
  public String editSubmittedCode(String username, int lobbyId) {
    return null;
  }

  @Override
  public SimulationResult getSimulationResult(String username, int lobbyId) {
    return null;
  }
}
