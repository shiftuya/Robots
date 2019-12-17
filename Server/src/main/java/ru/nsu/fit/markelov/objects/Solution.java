package ru.nsu.fit.markelov.objects;

import java.util.List;

public interface Solution {
    Level getLevel();
    List<Attempt> getAttempts();
}
