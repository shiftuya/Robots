package ru.nsu.fit.markelov.interfaces.server;

import ru.nsu.fit.markelov.interfaces.client.User;

/**
 * This is just a general idea of the interface
 */
public interface DatabaseHandler {
  User getPlayerByName(String name);

  void savePlayer(User user);


  // Important: Solutions include Simulation Results
  //List<Solution> getSolutionsOfPlayer(Player player);

  //void saveSolutionForPlayer(Player player, Solution solution);

  void removePlayer(User user);
}
