package ru.nsu.fit.markelov.mainmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.Pair;
import ru.nsu.fit.markelov.interfaces.client.User;

class Lobby1 implements LobbyExtended {
  private Date creationDate;
  private int requiredPlayersCnt;
  private Level level;
  private int id;

  private Map<UserExtended, LobbyPair<UserExtended, Boolean>> usersMap;
  private Map<UserExtended, String> solutionsMap;
  private List<UserExtended> usersList;

  private static class LobbyPair<K, V> implements Pair<K, V> {
    private K key;
    private V value;

    LobbyPair(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    public void setKey(K key) {
      this.key = key;
    }

    public void setValue(V value) {
      this.value = value;
    }
  }

  Lobby1(int id, Level level, int requiredPlayersCnt, UserExtended host) {
    if (requiredPlayersCnt < 1) {
      throw new ProcessingException("Impossible players count in lobby");
    }
    creationDate = new Date();
    //users = new ArrayList<>();
    usersList = new ArrayList<>();
    usersMap = new HashMap<>();
    solutionsMap = new HashMap<>();

    this.requiredPlayersCnt = requiredPlayersCnt;
    this.level = level;
    this.id = id;

    LobbyPair<UserExtended, Boolean> pair = new LobbyPair<>(host, false);
    //users.add(pair);
    usersMap.put(host, pair);
    usersList.add(host);
  }

  @Override
  public Date getCreationDate() {
    return creationDate;
  }

  @Override
  public List<UserExtended> getUsersWithoutPair() {
    return new ArrayList<>(usersMap.keySet());
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public int getCurrentPlayersAmount() {
    return usersList.size();
  }

  @Override
  public int getAcceptablePlayersAmount() {
    return requiredPlayersCnt;
  }

  @Override
  public List<Pair<User, Boolean>> getUsers() {
    List<Pair<User, Boolean>> list = new ArrayList<>();
    for (UserExtended user : usersList) {
      list.add(new LobbyPair<>(user, usersMap.get(user).getValue()));
    }
    return list;
  }

  @Override
  public Level getLevel() {
    return level;
  }

  @Override
  public boolean isReady() {
    if (getCurrentPlayersAmount() != getAcceptablePlayersAmount()) {
      return false;
    }
    for (Pair<UserExtended, Boolean> pair : usersMap.values()) {
      if (!pair.getValue()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void addPlayer(UserExtended player) {
    usersMap.put(player, new LobbyPair<>(player, false));
    usersList.add(player);
    solutionsMap.put(player, null);
  }

  @Override
  public void removePlayer(UserExtended player) {
    usersMap.remove(player);
    usersList.remove(player);
    solutionsMap.remove(player);
  }

  @Override
  public void submit(UserExtended user, String code) {
    solutionsMap.put(user, code);
    setReady(user, true);
  }

  @Override
  public void setReady(UserExtended player, boolean ready) {
    usersMap.get(player).setValue(ready);
  }

  @Override
  public String getCode(UserExtended user) {
    return solutionsMap.get(user);
  }

  @Override
  public Map<UserExtended, String> getSolutionsMap() {
    return solutionsMap;
  }
}
