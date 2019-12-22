package ru.nsu.fit.markelov.objects_hardcoded;

import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.Solution;

import java.util.ArrayList;
import java.util.List;

public class SolutionHardcoded implements Solution {

    private Level level;
    private List<SimulationResult> attempts;

    public SolutionHardcoded() {
        attempts = new ArrayList<>();
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public List<SimulationResult> getSimulationResults() {
        return attempts;
    }

    public void setAttempts(List<SimulationResult> attempts) {
        this.attempts = attempts;
    }
}
