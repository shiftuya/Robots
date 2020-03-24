package i.shatalov.teamproject.database;

public interface DataBaseHandler {
  PlayerClass getPlayerByName(String name);

  void savePlayer(PlayerClass playerClass) throws ClassNotFoundException;

  void saveLevel(LevelClass levelClass);
}
