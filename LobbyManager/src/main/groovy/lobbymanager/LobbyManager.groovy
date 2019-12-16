package lobbymanager

interface LobbyManager {
    void createLobby(Level level, int minPlayersCount, int maxPlayersCount, Player host)
    void leaveLobby(Player player, Lobby lobby)
    void simulateLobby(Lobby lobby)
    void addPlayerToLobby(Player player, Lobby lobby)
    void setPlayerReady(Player player, boolean ready)
   // void removeLobby(Lobby lobby)
    List<Lobby> getListOfLobbies()
}