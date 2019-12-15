package lobbymanager

class LobbyManagerClass implements LobbyManager {
    List<Lobby> lobbies

    LobbyManagerClass() {
        lobbies = new ArrayList<>()
    }

    @Override
    void createLobby(Level level, int minPlayersCount, int maxPlayersCount, Player initPlayer) {
        Lobby lobby = new LobbyClass(minPlayersCount, maxPlayersCount, level)
        lobby.addPlayer(initPlayer)
        lobbies.add(lobby)
    }

    @Override
    void addPlayerToLobby(Player player, Lobby lobby) {
        lobby.addPlayer(player)
    }

    @Override
    void removePlayerFromLobby(Player player) {
        if (player.getCurrentLobby() == null) {
            throw new IllegalStateException("The player is not in a lobby")
        }
        player.getCurrentLobby().removePlayer(player)
        player.setCurrentLobby(null)
    }

    @Override
    void simulateLobby(Lobby lobby) {
        lobby.simulate()
    }
}
