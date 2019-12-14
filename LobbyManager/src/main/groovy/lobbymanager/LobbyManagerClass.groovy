package lobbymanager

class LobbyManagerClass {
    List<LobbyClass> lobbies

    Map<PlayerClass, LobbyClass> playerLobbyMap // Do I need it?

    LobbyManagerClass() {
        lobbies = new ArrayList<>()
        playerLobbyMap = new HashMap<>()
    }

    LobbyClass createLobby(LevelClass level, int maxPlayersCount) {
        LobbyClass lobby = new LobbyClass(maxPlayersCount, level)
        lobbies.add(lobby)
        return lobby
    }

    void addPlayerToLobby(PlayerClass player, LobbyClass lobby) {
        lobby.addPlayer()
    }

    void removeLobby(LobbyClass lobby) {
        lobbies.remove(lobby)
    }
}
