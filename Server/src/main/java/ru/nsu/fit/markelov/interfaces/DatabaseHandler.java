package ru.nsu.fit.markelov.interfaces;

import java.util.List;

/**
 * This is just a general idea of the interface
 */
public interface DatabaseHandler {
  Player getPlayerByName(String name);

  void savePlayer(Player player);


  // Important: Solutions include Simulation Results
  List<Solution> getSolutionsOfPlayer(Player player);

  void saveSolutionForPlayer(Player player, Solution solution);

  void removePlayer(Player player);
}
