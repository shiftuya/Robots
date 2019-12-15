package lobbymanager

class PlayerClass implements Player {
    private int id
    private String name
    private List<SimulationResult> results
    private Lobby currentLobby
    private boolean isReady

    PlayerClass(int id, String name) {
        this.id = id
        this.name = name
        isReady = false
    }

    @Override
    int getId() {
        return id
    }

    @Override
    String getName() {
        return name
    }

    @Override
    List<SimulationResult> getResults() {
        return results
    }

    @Override
    Lobby getCurrentLobby() {
        return currentLobby
    }

    @Override
    void setCurrentLobby(Lobby lobby) {
        currentLobby = lobby
    }

    @Override
    boolean isReady() {
        return isReady
    }

    @Override
    void setReady(boolean ready) {
        isReady = ready
    }
}
