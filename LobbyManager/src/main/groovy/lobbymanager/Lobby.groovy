package lobbymanager

interface Lobby {
    int getMinPlayersCount()
    int getMaxPlayersCount()
    int getPlayersCount()
    Level getLevel()

    void addPlayer(Player player)
    void removePlayer(Player player)
    SimulationResult simulate()
}