package lobbymanager

class LobbyManagerClass implements LobbyManager {
    List<Lobby> lobbies

    Map<Integer, Lobby> idToLobby

    @Override
    List<Lobby> getListOfLobbies() {
        return lobbies
    }

    LobbyManagerClass() {
        lobbies = new ArrayList<>()
        idToLobby = new HashMap<>()
    }

    @Override
    void createLobby(Level level, int minPlayersCount, int maxPlayersCount, Player host) {
        int newId = idToLobby.keySet().stream().max(Comparator.comparingInt()).orElse(0)

        Lobby lobby = new LobbyClass(minPlayersCount, maxPlayersCount, level, newId)
        idToLobby.put(newId, lobby)

        lobby.addPlayer(host)
        lobbies.add(lobby)
    }

    @Override
    void addPlayerToLobby(Player player, Lobby lobby) {
        lobby.addPlayer(player)
    }

    @Override
    void leaveLobby(Player player, Lobby lobby) {
        /*if (player.getCurrentLobby() == null) {
            throw new IllegalStateException("The player is not in a lobby")
        }
        player.getCurrentLobby().removePlayer(player)
        player.setCurrentLobby(null)*/

        lobby.removePlayer(player)
        if (lobby.getPlayersCount() == 0) {
            lobbies.remove(lobby)
        }
    }

    @Override
    void simulateLobby(Lobby lobby) {
        lobby.simulate()
    }

    @Override
    void setPlayerReady(Player player, boolean ready) {
        player.setReady(ready)
    }

    /*@Override
    void removeLobby(Lobby lobby) {
        lobbies.remove(lobby)
    }*/
}
