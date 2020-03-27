package ru.nsu.fit.markelov.managers_hardcoded;

import ru.nsu.fit.markelov.interfaces.client.Solution;

import java.util.ArrayList;
import java.util.List;

public class UserManagerHardcoded {

    private List<Solution> solutions;

    public UserManagerHardcoded() {
        solutions = new ArrayList<>();
    }

    public void addSolution(Solution solution) {
        solutions.add(solution);
    }

    public List<Solution> getSolutions(String userName) {
        return solutions;
    }
}
