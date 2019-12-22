package ru.nsu.fit.markelov.interfaces;

//import com.sun.istack.internal.NotNull;

import java.util.List;

public interface Solution {
    /**
     * Returns the level.
     *
     * @return the level.
     */
    /*@NotNull*/
    Level getLevel();

    /**
     * Returns the list of simulation results.
     *
     * All the simulation results must be sorted by date (the newest one - in the head).
     *
     * @return the list of simulation results.
     */
    /*@NotNull*/
    List<SimulationResult> getSimulationResults();
}
