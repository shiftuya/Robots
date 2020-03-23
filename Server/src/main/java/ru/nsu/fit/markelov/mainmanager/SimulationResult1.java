package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.playback.Playback;

import java.util.Date;
import java.util.HashMap;

public class SimulationResult1 implements SimulationResult {
  // Id of simulation equal to id of the lobby.
  private int id;

  // Username to Success/Fail.
  private HashMap<String, Boolean> results;

  // Date of the simulation.
  private Date date;

  // Shared log.
  private String systemLog;

  // Username to privateLog.
  private HashMap<String, String> privateLogs;

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
  public Playback getPlayback(String username) {
    return null;
  }
}
