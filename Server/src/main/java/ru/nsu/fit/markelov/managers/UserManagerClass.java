package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Lobby;
import ru.nsu.fit.markelov.objects.Solution;

import java.util.ArrayList;
import java.util.List;

public class UserManagerClass implements UserManager {

    private List<Solution> solutions;

    public UserManagerClass() {
        solutions = new ArrayList<>();
    }

    @Override
    public void addSolutionHARDCODED(Solution solution) {
        solutions.add(solution);
    }

    @Override
    public List<Solution> getSolutions(String userName) {
        return solutions;
    }
}
