package lobbymanager

class LobbyManager {
    List<Lobby> lobbies

    Map<Player, Lobby> playerLobbyMap // Do I need it?

    LobbyManager() {
        lobbies = new ArrayList<>()
        playerLobbyMap = new HashMap<>()
    }

    Lobby createLobby(Level level, int maxPlayersCount) {
        Lobby lobby = new Lobby(maxPlayersCount, level)
        lobbies.add(lobby)
        return lobby
    }

    void addPlayerToLobby(Player player, Lobby lobby) {
        lobby.addPlayer()
    }

    void removeLobby(Lobby lobby) {
        lobbies.remove(lobby)
    }
}
