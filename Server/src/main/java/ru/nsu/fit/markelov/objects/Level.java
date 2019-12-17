package ru.nsu.fit.markelov.objects;

public interface Level {
    int getId();
    String getIconAddress();
    String getName();
    String getDifficulty();
    String getType();
    String getDescription();
    String getRules();
    String getGoal();
    int getMinPlayers();
    int getMaxPlayers();
}
