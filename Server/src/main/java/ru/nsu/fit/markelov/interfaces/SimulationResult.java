package ru.nsu.fit.markelov.interfaces;

import com.sun.istack.internal.NotNull;

public interface SimulationResult {
    /**
     * Returns whether the user's robot has reached the goal.
     *
     * @param username user's unique name.
     * @return         whether the user's robot has reached the goal.
     */
    boolean isSuccessful(String username);

    /**
     * Returns user's simulation log.
     *
     * @param username user's unique name.
     * @return         user's simulation log.
     */
    @NotNull
    String getLog(String username);
}
