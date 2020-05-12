package ru.nsu.fit.markelov.mainmanager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import ru.nsu.fit.markelov.interfaces.client.playback.Playback;

import java.util.Date;
import java.util.HashMap;
import ru.nsu.fit.markelov.interfaces.client.User;

public class SimulationResult1 implements SimulationResultExtended {
  // Id of simulation equal to id of the lobby.
  private int id;

  // Username to Success/Fail.
  private Map<String, Boolean> results;

  // Date of the simulation.
  private Date date;

  // Shared log.
  private String systemLog;

  // Username to privateLog.
  private Map<String, String> privateLogs;

  private Map<String, Playback> playbacks;

  private Map<String, UserExtended> usernameMap;

  private int levelId;

  public SimulationResult1(
      int simId,
      HashMap<String, Boolean> simResults,
      HashMap<String, String> _privateLogs,
      Date simDate) {
    id = simId;
    results = simResults;
    date = simDate;
    privateLogs = _privateLogs;
  }

  public SimulationResult1(int id, Map<String, Boolean> results, Date date,
      Map<String, String> privateLogs, Map<String, Playback> playbacks, int levelId) {
    this.id = id;
    this.results = results;
    this.date = date;
    this.privateLogs = privateLogs;
    this.playbacks = playbacks;
    this.levelId = levelId;
  }

  @Override
  public int getId() {
    return id;
  }

  /**
   * Returns if user passed test. Warning, user must have participated in this simulation.
   *
   * @param username user's unique name.
   * @return true if u private String systemLog;
   */
  @Override
  public boolean isSuccessful(String username) {
    return results.get(username);
  }

  @Override
  public Date getDate() {
    return date;
  }

  @Override
  public String getLog(String username) {
    try {
      return privateLogs.get(username);
    } catch (Exception e) {
      return "Error occurred while obtaining log";
    }
  }

  @Override
  public Playback getPlayback() {
    return null; // TODO
  }

  @Override
  public Set<String> getUserNames() {
    return results.keySet();
  }

  @Override
  public Iterable<User> getUsers() { // TODO put host in head
    return new ArrayList<>(usernameMap.values());
  }

  @Override
  public int getLevelId() {
    return levelId;
  }

  @Override
  public void putUsersMap(Map<String, UserExtended> map) {
    usernameMap = map;
  }
}
