package ru.nsu.fit.markelov.interfaces.server;

import ru.nsu.fit.markelov.interfaces.client.*;
import ru.nsu.fit.markelov.mainmanager.SimulationResultExtended;

import java.util.List;
import java.util.Map;

/** Simulator manager is used to control available simulator units and run tasks on them. */
public interface SimulatorManager {
  /**
   * Add new simulator unit in simulator pool. Checks if url is correct and if simulator is online.
   *
   * @param url location of new simulator unit.
   */
  void addSimulator(String url);

  /**
   * Add new simulator unit in simulator pool. Performs no checks.
   *
   * @param url
   */
  void addSimulatorFromDB(String url);

  /**
   * Remove simulator unit from the simulator pool.
   *
   * @param url location simulator unit to remove.
   */
  void removeSimulator(String url);

  /**
   * Get list of all available SU.
   *
   * @return list of simulator units.
   */
  List<String> getSimulatorsList();

  /**
   * Run simulation on available simulation unit.
   *
   * @param levelId name of the level.
   * @param lobbyId id of lobby, used to define id of simulation.
   * @param solutions map of players and their solutions.
   * @return result of the simulation.
   * @throws ru.nsu.fit.markelov.simulator.MissingSimulationUnits if there are no active Simulation
   *     Units.
   */
  SimulationResultExtended runSimulation(String levelId, int lobbyId, Map<User, String> solutions);

  /**
   * Add a new level. If a level with same name and language exists then does nothing.
   *
   * @param name name of level to be referred.
   * @param language language of the level.
   * @param levelSrc code of the level.
   * @param resources list of resources to be stored along.
   */
  void addLevel(String name, String language, String levelSrc, List<Resource> resources);

  /**
   * Add a new level. If a level with same name and language exists then does nothing.
   *
   * @param name name of level to be referred.
   * @param language language of the level.
   * @param levelSrc code of the level.
   * @param resources list of resources to be stored along.
   * @param url url of the simulator
   */
  void addLevelOnSimulator(
      String name, String language, String levelSrc, List<Resource> resources, String url);

  /**
   * Remove a level.
   *
   * @param name name of the level to remove.
   * @param language language of the level.
   */
  void removeLevel(String name, String language);

  /**
   * Update existing level source code.
   *
   * @param name name of the level.
   * @param levelSrc new source code of the level.
   * @param language language of the level.
   */
  void updateLevel(String name, String language, String levelSrc, List<Resource> resources);

  /**
   * Check if solution can be compiled;
   *
   * @param language language of the solution.
   * @param solutionSrc code of the solution.
   * @return result of compilation.
   */
  CompileResult checkCompilation(String language, String solutionSrc);
}
