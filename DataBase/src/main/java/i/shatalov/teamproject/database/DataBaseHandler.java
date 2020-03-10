package i.shatalov.teamproject.database;

public interface DataBaseHandler {
  PlayerClass getPlayerByName(String name);

  void savePlayer(PlayerClass playerClass);

  void saveLevel(LevelClass levelClass);
}
