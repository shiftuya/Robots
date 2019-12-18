package ru.nsu.fit.markelov.interfaces;

import java.util.List;

public interface Solution {
    /**
     * Returns the level.
     *
     * @return the level.
     */
    Level getLevel();

    /**
     * Returns the list of simulation attempts.
     *
     * @return the list of simulation attempts.
     */
    List<Attempt> getAttempts();
}
