package ru.nsu.fit.markelov.mainmanager;

import java.util.List;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.Solution;

public class Solution1 implements Solution {
  Level level;
  List<SimulationResult> simulationResults;

  @Override
  public Level getLevel() {
    return level;
  }

  @Override
  public List<SimulationResult> getSimulationResults() {
    return simulationResults;
  }
}
