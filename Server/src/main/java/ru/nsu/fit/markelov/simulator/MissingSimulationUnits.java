package ru.nsu.fit.markelov.simulator;

public class MissingSimulationUnits extends RuntimeException {
  public MissingSimulationUnits(String errorMessage) {
    super(errorMessage);
  }
}
