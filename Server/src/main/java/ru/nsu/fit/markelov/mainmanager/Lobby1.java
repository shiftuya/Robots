package ru.nsu.fit.markelov.mainmanager;

import java.util.LinkedList;
import java.util.List;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Lobby;
import ru.nsu.fit.markelov.interfaces.Player;

class Lobby1 implements Lobby {
  private int id;
  private List<Player> players;
  private Level level;

  private int acceptablePlayersAmount;

  boolean addPlayer(Player player) {
    if (getCurrentPlayersAmount() >= getAcceptablePlayersAmount() || players.contains(player)) {
      return false;
    }

    players.add(player);

    return true;
  }

  boolean removePlayer(Player player) {
    return players.remove(player);
  }

  Lobby1(int id, Level level, int acceptablePlayersAmount) {
    players = new LinkedList<>();
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
    return null;
  }

  @Override
  public String getHostName() {
    return null;
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
