package ru.nsu.fit.markelov.interfaces.client;

import java.util.List;

public interface Lobby {
    /**
     * Returns the unique lobby id.
     *
     * @return the unique lobby id.
     */
    int getId();

    /**
     * Returns the host avatar address.
     *
     * @return the host avatar address.
     */
    String getHostAvatarAddress();

    /**
     * Returns the host name.
     *
     * @return the host name.
     */
    String getHostName();

    /**
     * Returns the amount of players present in the lobby.
     *
     * @return the amount of players present in the lobby.
     */
    int getCurrentPlayersAmount();

    /**
     * Returns the maximal amount of players for this lobby.
     *
     * @return the maximal amount of players for this lobby.
     */
    int getAcceptablePlayersAmount();

    /**
     * Returns the list of players.
     *
     * @return the list of players.
     */
    List<Player> getPlayers();

    /**
     * Returns the level.
     *
     * @return the level.
     */
    Level getLevel();
}
