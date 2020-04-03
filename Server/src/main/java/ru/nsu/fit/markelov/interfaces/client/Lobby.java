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
     * Returns the list of pairs (User, isSubmitted).
     *
     * A host-user must be in the head of the collection.
     *
     * @return the list of pairs (User, isSubmitted).
     */
    List<Pair<User, Boolean>> getUsers();

    /**
     * Returns the level.
     *
     * @return the level.
     */
    Level getLevel();
}
