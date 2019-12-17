package ru.nsu.fit.markelov.objects;

import java.util.List;

public interface Lobby {
    int getId();
    String getHostAvatarAddress();
    String getHostName();
    int getCurrentPlayersAmount();
    int getAcceptablePlayersAmount();
    List<Player> getPlayers();
    Level getLevel();
}
