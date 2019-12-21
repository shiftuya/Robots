package lobbymanager

import lobbymanager.Player
import lobbymanager.SimulationResult

interface SimulatorManager {
    public boolean addSimulator(String url);

    public boolean removeSimulator(String url);

    public ArrayList<String> getSimulatorsList();

    public Map<Player, SimulationResult> runSimulation(String levelId, Map<Player, String> solutions);
}