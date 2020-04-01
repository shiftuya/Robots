package ru.nsu.fit.markelov.mainmanager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.Pair;
import ru.nsu.fit.markelov.interfaces.client.User;

class Lobby1 implements Lobby {
  private int id;
  private List<User> users;
  private Level level;


  private Map<User, String> solutions;

  private int acceptablePlayersAmount;

  Map<User, String> getSolutions() {
    return solutions;
  }



  private User getHost() {
    return users.get(0);
  }

  boolean isReady() {return false;/*TODO
    for (User user : users) {
      if (!user.isSubmitted()) return false;
    }
    return solutions.size() == users.size() && users.size() == acceptablePlayersAmount;*/
  }

  boolean addPlayer(User user) {
    if (getCurrentPlayersAmount() >= getAcceptablePlayersAmount() || users.contains(user)) {
      return false;
    }

    users.add(user);

    return true;
  }

  boolean addSolution(User1 player, String solution) {
    if (!users.contains(player)) {
      return false;
    }
    solutions.put(player, solution);
    player.setSolutionCode(solution);
    player.setSubmitted(true);
    return true;
  }

  String getSolution(User user) {
    return solutions.get(user);
  }

  void removeSolution(User user) {
    solutions.remove(user);
  }

  boolean removePlayer(User user) {
    solutions.remove(user);
    return users.remove(user);
  }

  Lobby1(int id, Level level, int acceptablePlayersAmount) {
    users = new LinkedList<>();
    solutions = new HashMap<>();
    this.id = id;
    this.level = level;
    this.acceptablePlayersAmount = acceptablePlayersAmount;
  }

  @Override
  public int getId() {
    return id;
  }

  /*@Override
  public String getHostAvatarAddress() {
    return getHost().getAvatarAddress();
  }

  @Override
  public String getHostName() {
    return getHost().getName();
  }*/

  @Override
  public int getCurrentPlayersAmount() {
    return users.size();
  }

  @Override
  public int getAcceptablePlayersAmount() {
    return acceptablePlayersAmount;
  }

  @Override
  public List<Pair<User, Boolean>> getUsers() {//TODO
    return null;
  }

  @Override
  public Level getLevel() {
    return level;
  }
}
