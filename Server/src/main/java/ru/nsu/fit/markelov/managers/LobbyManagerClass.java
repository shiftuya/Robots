package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyManagerClass implements LobbyManager {

    private List<Lobby> lobbies;

    public LobbyManagerClass() {
        lobbies = new ArrayList<>();
    }

    @Override
    public void addLobbyHARDCODED(Lobby lobby) {
        lobbies.add(lobby);
    }

    @Override
    public List<Lobby> getLobbies() {
        return lobbies;
    }

    @Override
    public Lobby joinLobby(String userName, int lobbyID) {
        return lobbies.get(1);
    }

    @Override
    public Lobby createLobby(String userName, int levelID, int playersAmount) {
        return lobbies.get(1);
    }

    @Override
    public List<Lobby> leaveLobby(String userName, int lobbyID) {
        return null;
    }
}
