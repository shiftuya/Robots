package i.shatalov.teamproject.database;

public interface DataBaseHandler {
  Player getPlayerName(String name);

  void savePlayer(Player player);

  void saveLevel(Level level);
}
