package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.SimulationResult;

public class SimulationResult1 implements SimulationResult {
  private int id;


  @Override
  public int getId() {
    return id;
  }

  @Override
  public boolean isSuccessful(String username) {
    return false;
  }

  @Override
  public String getDate() {
    return null;
  }

  @Override
  public String getLog(String username) {
    return null;
  }
}
