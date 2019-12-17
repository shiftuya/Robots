package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Level;
import ru.nsu.fit.markelov.objects.LevelClass;
import ru.nsu.fit.markelov.objects.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LevelManagerClass implements LevelManager {

    private List<Level> levels;

    public LevelManagerClass() {
        levels = new ArrayList<>();
    }

    @Override
    public List<Level> getLevels() {
        return levels;
    }

    @Override
    public void addLevelHARDCODED(Level level) {
        levels.add(level);
    }
}
