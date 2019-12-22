package ru.nsu.fit.markelov.interfaces;

import java.util.ArrayList;
import java.util.Map;

public interface SimulatorManager {

  boolean addSimulator(String url);

  boolean removeSimulator(String url);

  ArrayList<String> getSimulatorsList();

  SimulationResult runSimulation(String levelId, int lobbyId, Map<Player, String> solutions);
}
