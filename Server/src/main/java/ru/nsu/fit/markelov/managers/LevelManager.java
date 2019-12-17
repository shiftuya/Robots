package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Level;
import ru.nsu.fit.markelov.objects.Lobby;

import java.util.List;

public interface LevelManager {
    /**
     * Returns list of created levels.
     *
     * @return list of created levels.
     */
    List<Level> getLevels();



    // ----- (for HARDCODING only?) -----

    /**
     * Adds level to the list of levels.
     *
     * @param level a level to add.
     */
    void addLevelHARDCODED(Level level);
}
