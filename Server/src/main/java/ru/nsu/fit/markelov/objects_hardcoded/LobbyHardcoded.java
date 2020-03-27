package ru.nsu.fit.markelov.objects_hardcoded;

import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.Player;

import java.util.ArrayList;
import java.util.List;

public class LobbyHardcoded implements Lobby {

    private int id;
    private String hostAvatarAddress;
    private String hostName;
    private int currentPlayersAmount;
    private int acceptablePlayersAmount;

    private List<Player> players;
    private Level level;

    public LobbyHardcoded() {
        players = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getHostAvatarAddress() {
        return hostAvatarAddress;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public int getCurrentPlayersAmount() {
        return currentPlayersAmount;
    }

    @Override
    public int getAcceptablePlayersAmount() {
        return acceptablePlayersAmount;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHostAvatarAddress(String hostAvatarAddress) {
        this.hostAvatarAddress = hostAvatarAddress;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setCurrentPlayersAmount(int currentPlayersAmount) {
        this.currentPlayersAmount = currentPlayersAmount;
    }

    public void setAcceptablePlayersAmount(int acceptablePlayersAmount) {
        this.acceptablePlayersAmount = acceptablePlayersAmount;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
