package lobbymanager

interface Player {
    int getId()
    String getName()

    List<SimulationResult> getResults()

   // Lobby getCurrentLobby() // Null if player is not in a lobby
    //void setCurrentLobby(Lobby lobby)

    boolean isReady()
    void setReady(boolean ready)
}