package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Level;
import ru.nsu.fit.markelov.objects.Solution;

import java.util.List;

public interface UserManager {
    /**
     * Returns list of all the solutions of specified user.
     *
     * @param userName user's unique name.
     * @return         list of all the solutions of specified user.
     */
    List<Solution> getSolutions(String userName);



    // ----- (for HARDCODING only?) -----

    /**
     * Adds solution to the list of solutions.
     *
     * @param solution a solution to add.
     */
    void addSolutionHARDCODED(Solution solution);
}
