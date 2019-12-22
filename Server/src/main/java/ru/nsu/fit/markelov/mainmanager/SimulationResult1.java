package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.SimulationResult;

import java.util.Date;

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
  public Date getDate() {
    return null;
  }

  @Override
  public String getLog(String username) {
    return null;
  }
}
