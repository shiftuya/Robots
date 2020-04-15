package ru.nsu.fit.markelov.mainmanager;

import java.util.Map;
import java.util.Set;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

public interface SimulationResultExtended extends SimulationResult {
  Set<String> getUserNames();
  int getLevelId();
  String getLog(String username);
  void putUsersMap(Map<String, UserExtended> map);
}
