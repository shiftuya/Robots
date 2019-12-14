package lobbymanager

interface Player {
    int getId()
    String getName()

    List<SimulationResult> getResults()

    Lobby getCurrentLobby()
    void setCurrentLobby(Lobby lobby)

    boolean isReady()
    void setReady(boolean ready)

}