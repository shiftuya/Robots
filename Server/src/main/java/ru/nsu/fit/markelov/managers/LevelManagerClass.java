package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.interfaces.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelManagerClass {

    private List<Level> levels;

    public LevelManagerClass() {
        levels = new ArrayList<>();
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }
}
