package ru.nsu.fit.markelov.httphandlers.inputs;

import ru.nsu.fit.markelov.interfaces.client.Resource;
import ru.nsu.fit.markelov.httphandlers.util.LevelResource;

import java.util.ArrayList;
import java.util.List;

public class LevelInput {

    private String name;
    private String difficulty;
    private String minPlayers;
    private String maxPlayers;
    private LevelResource iconResource;
    private String description;
    private String rules;
    private String goal;
    private List<Resource> levelResources;
    private String code;

    public LevelInput() {
        iconResource = new LevelResource();
        levelResources = new ArrayList<>();
    }

    public String getError() {
        String error = "";

        if (name.isEmpty()) {
            error += "Name field cannot be empty.\n";
        }

        if (difficulty.isEmpty()) {
            error += "Difficulty field cannot be empty.\n";
        }

        if (minPlayers.isEmpty()) {
            error += "Players field cannot be empty.\n";
        }

        try {
            int minPlayers = getMinPlayers();
            if (minPlayers < 1 || minPlayers > 10) {
                error += "Players must be from 1 to 10.\n";
            }
        } catch (NumberFormatException e) {
            error += "Players must be a number.\n";
        }

        try {
            int maxPlayers = getMaxPlayers();
            if (maxPlayers < 1 || maxPlayers > 10) {
                error += "Players must be from 1 to 10.\n";
            }
        } catch (NumberFormatException e) {
            error += "Players must be a number.\n";
        }

        if (description.isEmpty()) {
            error += "Description field cannot be empty.\n";
        }

        if (rules.isEmpty()) {
            error += "Rules field cannot be empty.\n";
        }

        if (goal.isEmpty()) {
            error += "Goal field cannot be empty.\n";
        }

        if (code.isEmpty()) {
            error += "Code field cannot be empty.\n";
        }

        return error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getMinPlayers() {
        return Integer.parseInt(minPlayers);
    }

    public void setMinPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return Integer.parseInt(maxPlayers);
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public LevelResource getIconResource() {
        return iconResource;
    }

    public void setIconResource(LevelResource iconResource) {
        this.iconResource = iconResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<Resource> getLevelResources() {
        return levelResources;
    }

    public void setLevelResources(List<Resource> levelResources) {
        this.levelResources = levelResources;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
