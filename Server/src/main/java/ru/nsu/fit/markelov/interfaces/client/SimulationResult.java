package ru.nsu.fit.markelov.interfaces.client;

import ru.nsu.fit.markelov.interfaces.client.playback.Playback;

import java.util.Date;

public interface SimulationResult {
    /**
     * Returns unique simulation result id.
     *
     * @return unique simulation result id.
     */
    int getId();

    /**
     * Returns a collection of users of the simulation.
     *
     * A host-user must be in the head of the collection.
     *
     * @return a collection of users of the simulation.
     */
    Iterable<User> getUsers();

    /**
     * Returns whether user robot has reached the goal.
     *
     * @param username unique user name.
     * @return whether user robot has reached the goal.
     */
    boolean isSuccessful(String username);

    /**
     * Returns simulation result date.
     *
     * @return simulation result date.
     */
    Date getDate();

    /**
     * Returns simulation playback.
     *
     * @return simulation playback.
     */
    Playback getPlayback();
}
