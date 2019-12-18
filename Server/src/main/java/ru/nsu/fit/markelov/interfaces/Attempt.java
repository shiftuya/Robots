package ru.nsu.fit.markelov.interfaces;

import com.sun.istack.internal.NotNull;

public interface Attempt {
    /**
     * Returns the unique id of simulation attempt.
     *
     * @return the unique id of simulation attempt.
     */
    int getId();

    /**
     * Returns the date of simulation attempt.
     *
     * @return the date of simulation attempt.
     */
    @NotNull
    String getDate();

    /**
     * Returns whether the robot has reached the goal.
     *
     * @return whether the robot has reached the goal.
     */
    boolean isSuccessful();
}
