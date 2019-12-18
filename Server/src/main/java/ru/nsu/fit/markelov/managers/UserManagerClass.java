package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.interfaces.Solution;

import java.util.ArrayList;
import java.util.List;

public class UserManagerClass {

    private List<Solution> solutions;

    public UserManagerClass() {
        solutions = new ArrayList<>();
    }

    public void addSolution(Solution solution) {
        solutions.add(solution);
    }

    public List<Solution> getSolutions(String userName) {
        return solutions;
    }
}
