package ru.nsu.fit.markelov.interfaces.server;

import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Player;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

import java.util.List;
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
  List<String> getSimulatorsList();

  /**
   * Run simulation on available simulation unit.
   *
   * @param levelId filename of the level.
   * @param lobbyId id of lobby, used to define id of simulation.
   * @param solutions map of players and their solutions.
   * @return result of the simulation.
   * @throws ru.nsu.fit.markelov.simulator.MissingSimulationUnits if there are no active Simulation
   *     Units.
   */
  SimulationResult runSimulation(String levelId, int lobbyId, Map<Player, String> solutions);

  /**
   * Get a list of all active levels.
   *
   * @return active levels.
   */
  List<Level> getLevels();

  /**
   * Add a new level. If a level with same name and language exists then does nothing.
   *
   * @param name name of level to be referred.
   * @param source code of te level.
   * @param language language of the level.
   * @return true if added, false if a level with the same name and language exists.
   */
  boolean addLevel(String name, String source, String language);

  /**
   * Remove a level.
   *
   * @param name name of the level to remove.
   * @param language language of the level.
   * @return true if the level was removed, false if no such level exist.
   */
  boolean removeLevel(String name, String language);

  /**
   * Update existing level source code.
   *
   * @param name name of the level.
   * @param source new source code of the level.
   * @param language language of the level.
   * @return true if level was updated successfully.
   */
  boolean updateLevel(String name, String source, String language);
}
