package ru.nsu.fit.markelov.interfaces;

import java.util.ArrayList;
import java.util.Map;

/** Simulator manager is used to control available simulator units and run tasks on them. */
public interface SimulatorManager {
  /**
   * Add new simulator unit in simulator pool.
   *
   * @param url location of new simulator unit.
   * @return true if SU was added, false otherwise.
   */
  boolean addSimulator(String url);

  /**
   * Remove simulator unit from the simulator pool.
   *
   * @param url location simulator unit to remove.
   * @return true if SU was added, false otherwise.
   */
  boolean removeSimulator(String url);

  /**
   * Get list of all available SU.
   *
   * @return list of simulator units.
   */
  ArrayList<String> getSimulatorsList();

  /**
   * Run simulation on available simulation unit.
   *
   * @param levelId filename of the level.
   * @param lobbyId id of lobby, used to define id of simulation.
   * @param solutions map of players and their solutions.
   * @return result of the simulation.
   */
  SimulationResult runSimulation(String levelId, int lobbyId, Map<Player, String> solutions);
}
