package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.Level;

public class Level1 implements Level {
  private int id;
  private String iconAddress;
  private String name;
  private String difficulty;
  private String type;
  private String description;
  private String rules;
  private String goal;
  private int minPlayers;
  private int maxPlayers;

  Level1 setId(int id) {
    this.id = id;
    return this;
  }

  Level1 setIconAddress(String iconAddress) {
    this.iconAddress = iconAddress;
    return this;
  }

  Level1 setName(String name) {
    this.name = name;
    return this;
  }

  Level1 setDifficulty(String difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  Level1 setType(String type) {
    this.type = type;
    return this;
  }

  Level1 setDescription(String description) {
    this.description = description;
    return this;
  }

  Level1 setRules(String rules) {
    this.rules = rules;
    return this;
  }

  Level1 setGoal(String goal) {
    this.goal = goal;
    return this;
  }

  Level1 setMinPlayers(int minPlayers) {
    this.minPlayers = minPlayers;
    return this;
  }

  Level1 setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getIconAddress() {
    return iconAddress;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDifficulty() {
    return difficulty;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String getRules() {
    return rules;
  }

  @Override
  public String getGoal() {
    return goal;
  }

  @Override
  public int getMinPlayers() {
    return minPlayers;
  }

  @Override
  public int getMaxPlayers() {
    return maxPlayers;
  }
}
