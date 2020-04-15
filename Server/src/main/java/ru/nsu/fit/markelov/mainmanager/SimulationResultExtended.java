package ru.nsu.fit.markelov.mainmanager;

import java.util.Set;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

public interface SimulationResultExtended extends SimulationResult {
  Set<String> getUsers();
  int getLevelId();
}
