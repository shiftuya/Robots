package lobbymanager

interface LobbyManager {
    void createLobby(Level level, int minPlayersCount, int maxPlayersCount, Player initPlayer)

    void removePlayerFromLobby(Player player)

    void simulateLobby(Lobby lobby)

    void addPlayerToLobby(Player player, Lobby lobby)



}