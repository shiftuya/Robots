package ru.nsu.fit.markelov.objects;

import ru.nsu.fit.markelov.interfaces.Attempt;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Solution;

import java.util.ArrayList;
import java.util.List;

public class SolutionClass implements Solution {

    private Level level;
    private List<Attempt> attempts;

    public SolutionClass() {
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
    public List<Attempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<Attempt> attempts) {
        this.attempts = attempts;
    }
}
