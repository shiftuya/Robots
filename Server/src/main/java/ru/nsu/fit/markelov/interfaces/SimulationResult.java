package ru.nsu.fit.markelov.interfaces;

//import com.sun.istack.internal.NotNull;

import java.util.Date;

public interface SimulationResult {
    /**
     * Returns the unique id of simulation result.
     *
     * @return the unique id of simulation result.
     */
    int getId();

    /**
     * Returns whether the user's robot has reached the goal.
     *
     * @param username user's unique name.
     * @return         whether the user's robot has reached the goal.
     */
    boolean isSuccessful(/*@NotNull*/ String username);

    /**
     * Returns the date of simulation result.
     *
     * @return the date of simulation result.
     */
    /*@NotNull*/
    Date getDate();

    /**
     * Returns user's simulation log.
     *
     * @param username user's unique name.
     * @return         user's simulation log.
     */
    /*@NotNull*/
    String getLog(/*@NotNull*/ String username);
}
