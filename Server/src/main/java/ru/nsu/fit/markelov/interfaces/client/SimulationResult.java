package ru.nsu.fit.markelov.interfaces.client;

import java.util.Date;

public interface SimulationResult {
    /**
     * Returns unique simulation result id.
     *
     * @return unique simulation result id.
     */
    int getId();

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
     * Returns user simulation log.
     *
     * @param username unique user name.
     * @return user simulation log.
     */
    String getLog(String username);

    /**
     * Returns simulation playback.
     *
     * @param username unique user name.
     * @return simulation playback.
     */
    Playback getPlayback(String username);
}
