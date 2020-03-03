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
     * Returns the list of simulation results.
     *
     * All the simulation results must be sorted by date (the newest one - in the head).
     *
     * @return the list of simulation results.
     */
    List<SimulationResult> getSimulationResults();
}
