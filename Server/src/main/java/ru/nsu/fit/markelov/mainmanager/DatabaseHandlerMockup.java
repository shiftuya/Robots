package ru.nsu.fit.markelov.mainmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.nsu.fit.markelov.interfaces.DatabaseHandler;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.Solution;

public class DatabaseHandlerMockup implements DatabaseHandler {

  private Map<String, Player> namePlayerMap;
  private Map<Player, List<Solution>> playerSolutionsMap;


  public DatabaseHandlerMockup() {
    namePlayerMap = new HashMap<>();
    playerSolutionsMap = new HashMap<>();

    Player player = new Player1("/images/person-icon.png", "Player");
    savePlayer(player);
  }

  @Override
  public Player getPlayerByName(String name) {
    return namePlayerMap.get(name);
  }

  @Override
  public void savePlayer(Player player) {
    namePlayerMap.put(player.getName(), player);
    playerSolutionsMap.put(player, new ArrayList<>());
  }

  @Override
  public List<Solution> getSolutionsOfPlayer(Player player) {
    return playerSolutionsMap.get(player);
  }

  @Override
  public void saveSolutionForPlayer(Player player, Solution solution) {
    if (playerSolutionsMap.containsKey(player)) {
      playerSolutionsMap.get(player).add(solution);
    }
  }

  @Override
  public void removePlayer(Player player) {
    namePlayerMap.remove(player.getName());
    playerSolutionsMap.remove(player);
  }
}
