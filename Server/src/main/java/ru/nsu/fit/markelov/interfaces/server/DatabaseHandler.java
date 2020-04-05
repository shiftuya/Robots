package ru.nsu.fit.markelov.interfaces.server;

import java.util.List;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.mainmanager.SimulationResultExtended;
import ru.nsu.fit.markelov.mainmanager.UserExtended;


public interface DatabaseHandler {
  List<UserExtended> getUsers();

  UserExtended getUserByName(String name);

  void saveUser(UserExtended user);

  void removeUser(UserExtended user);

  List<String> getSimulatorsUrls();

  void removeSimulatorUrl(String url);

  void saveSimulatorUrl(String url);

  void saveLevel(Level level);

  void removeLevel(Level level);

  List<Level> getLevels();

  Level getLevelById(int id);

  void addSimulationResult(SimulationResultExtended result);

  List<SimulationResultExtended> getSimulationResults();
}
