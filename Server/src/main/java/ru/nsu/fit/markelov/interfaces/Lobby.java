package ru.nsu.fit.markelov.interfaces;

import com.sun.istack.internal.NotNull;

import java.util.List;

public interface Lobby {
    /**
     * Returns the lobby's unique id.
     *
     * @return the lobby's unique id.
     */
    int getId();

    /**
     * Returns the address of host's avatar icon.
     *
     * @return the address of host's avatar icon.
     */
    @NotNull
    String getHostAvatarAddress();

    /**
     * Returns the host's name.
     *
     * @return the host's name.
     */
    @NotNull
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
    @NotNull
    List<Player> getPlayers();

    /**
     * Returns the level.
     *
     * @return the level.
     */
    @NotNull
    Level getLevel();
}
