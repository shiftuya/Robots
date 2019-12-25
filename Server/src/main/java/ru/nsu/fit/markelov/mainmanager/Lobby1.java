package ru.nsu.fit.markelov.mainmanager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Lobby;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;

class Lobby1 implements Lobby {
  private int id;
  private List<Player> players;
  private Level level;


  private Map<Player, String> solutions;

  private int acceptablePlayersAmount;

  Map<Player, String> getSolutions() {
    return solutions;
  }



  private Player getHost() {
    return players.get(0);
  }

  boolean isReady() {
    for (Player player : players) {
      if (!player.isSubmitted()) return false;
    }
    return solutions.size() == players.size() && players.size() == acceptablePlayersAmount;
  }

  boolean addPlayer(Player player) {
    if (getCurrentPlayersAmount() >= getAcceptablePlayersAmount() || players.contains(player)) {
      return false;
    }

    players.add(player);

    return true;
  }

  boolean addSolution(Player1 player, String solution) {
    if (!players.contains(player)) {
      return false;
    }
    solutions.put(player, solution);
    player.setSolutionCode(solution);
    player.setSubmitted(true);
    return true;
  }

  String getSolution(Player player) {
    return solutions.get(player);
  }

  void removeSolution(Player player) {
    solutions.remove(player);
  }

  boolean removePlayer(Player player) {
    solutions.remove(player);
    return players.remove(player);
  }

  Lobby1(int id, Level level, int acceptablePlayersAmount) {
    players = new LinkedList<>();
    solutions = new HashMap<>();
    this.id = id;
    this.level = level;
    this.acceptablePlayersAmount = acceptablePlayersAmount;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getHostAvatarAddress() {
    return getHost().getAvatarAddress();
  }

  @Override
  public String getHostName() {
    return getHost().getName();
  }

  @Override
  public int getCurrentPlayersAmount() {
    return players.size();
  }

  @Override
  public int getAcceptablePlayersAmount() {
    return acceptablePlayersAmount;
  }

  @Override
  public List<Player> getPlayers() {
    return players;
  }

  @Override
  public Level getLevel() {
    return level;
  }
}
