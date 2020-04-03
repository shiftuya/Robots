package ru.nsu.fit.markelov.objects_hardcoded;

import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.Pair;
import ru.nsu.fit.markelov.interfaces.client.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LobbyHardcoded implements Lobby {

    private int id;
    /*private String hostAvatarAddress;
    private String hostName;*/
    private int currentPlayersAmount;
    private int acceptablePlayersAmount;

    private List<User> users;
    private Level level;

    public LobbyHardcoded() {
        users = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    /*@Override
    public String getHostAvatarAddress() {
        return hostAvatarAddress;
    }

    @Override
    public String getHostName() {
        return hostName;
    }*/

    @Override
    public int getCurrentPlayersAmount() {
        return currentPlayersAmount;
    }

    @Override
    public int getAcceptablePlayersAmount() {
        return acceptablePlayersAmount;
    }

    @Override
    public List<Pair<User, Boolean>> getUsers() {
        return users.stream()
            .map(user -> new Pair<User, Boolean>() {
                @Override
                public User getKey() {
                    return user;
                }

                @Override
                public Boolean getValue() {
                    return true;
                }
            }).collect(Collectors.toList());
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public void setHostAvatarAddress(String hostAvatarAddress) {
        this.hostAvatarAddress = hostAvatarAddress;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }*/

    public void setCurrentPlayersAmount(int currentPlayersAmount) {
        this.currentPlayersAmount = currentPlayersAmount;
    }

    public void setAcceptablePlayersAmount(int acceptablePlayersAmount) {
        this.acceptablePlayersAmount = acceptablePlayersAmount;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
