package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.interfaces.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyManagerClass {

    private List<Lobby> lobbies;

    public LobbyManagerClass() {
        lobbies = new ArrayList<>();
    }

    public void addLobby(Lobby lobby) {
        lobbies.add(lobby);
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public Lobby joinLobby(String userName, int lobbyID) {
        return lobbies.get(1);
    }

    public Lobby createLobby(String userName, int levelID, int playersAmount) {
        return lobbies.get(1);
    }

    public boolean leaveLobby(String userName, int lobbyID) {
        return true;
    }

    public Lobby refreshLobby(String userName, int lobbyID) {
        return lobbies.get(1);
    }
}
