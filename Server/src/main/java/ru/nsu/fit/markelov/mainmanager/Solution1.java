package ru.nsu.fit.markelov.mainmanager;

import java.util.LinkedList;
import java.util.List;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.client.Solution;

/**
 * Contains all simulation results for a player at a given level
 */
public class Solution1 implements Solution {
  Level level;
  List<SimulationResult> simulationResults;

  public Solution1(Level level, SimulationResult simulationResult) {
    this.level = level;
    simulationResults = new LinkedList<>();
    simulationResults.add(simulationResult);
  }

  @Override
  public Level getLevel() {
    return level;
  }

  @Override
  public List<SimulationResult> getSimulationResults() {
    return simulationResults;
  }
}
