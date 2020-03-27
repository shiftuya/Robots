package ru.nsu.fit.markelov.managers_hardcoded;

import ru.nsu.fit.markelov.interfaces.client.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelManagerHardcoded {

    private List<Level> levels;

    public LevelManagerHardcoded() {
        levels = new ArrayList<>();
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }
}
