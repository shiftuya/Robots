package i.shatalov.teamproject.database;

public interface DataBaseHandler {
  PlayerClass getPlayerByName(String name) throws ClassNotFoundException;

  void savePlayer(PlayerClass playerClass) throws ClassNotFoundException;

  void saveLevel(LevelClass levelClass) throws ClassNotFoundException;
}
