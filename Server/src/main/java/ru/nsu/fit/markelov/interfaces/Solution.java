package ru.nsu.fit.markelov.interfaces;

import com.sun.istack.internal.NotNull;

import java.util.List;

public interface Solution {
    /**
     * Returns the level.
     *
     * @return the level.
     */
    @NotNull
    Level getLevel();

    /**
     * Returns the list of simulation attempts.
     *
     * @return the list of simulation attempts.
     */
    @NotNull
    List<Attempt> getAttempts();
}
