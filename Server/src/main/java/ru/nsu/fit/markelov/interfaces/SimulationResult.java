package ru.nsu.fit.markelov.interfaces;

import java.util.Date;

public interface SimulationResult {
    /**
     * Returns the unique id of simulation result.
     *
     * @return the unique id of simulation result.
     */
    int getId();

    /**
     * Returns whether the user robot has reached the goal.
     *
     * @param username user unique name.
     * @return         whether the user robot has reached the goal.
     */
    boolean isSuccessful(String username);

    /**
     * Returns the date of simulation result.
     *
     * @return the date of simulation result.
     */
    Date getDate();

    /**
     * Returns user simulation log.
     *
     * @param username user unique name.
     * @return         user simulation log.
     */
    String getLog(String username);
}
