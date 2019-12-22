package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;

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

  public SimulationResult1(int simId, HashMap<String, Boolean> simResults, Date simDate) {
    id = simId;
    results = simResults;
    date = simDate;
  }

  @Override
  public int getId() {
    return id;
  }

  /**
   * Returns if user passed test. Warning, user must have participated in this simulation.
   *
   * @param username user's unique name.
   * @return true if user reached goal.
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
    return null;
  }
}
