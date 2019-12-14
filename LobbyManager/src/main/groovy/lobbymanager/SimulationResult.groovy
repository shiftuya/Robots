package lobbymanager

interface SimulationResult {
    Date getDate()
    boolean isSuccessful()
    SimulationPlayback getPlayback()
    SimulationLog getLog()
}