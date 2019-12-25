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
  private String filename;

  Level1(int id, String iconAddress, String name, String difficulty, String type,
      String description, String rules, String goal, int minPlayers, int maxPlayers, String filename) {
    this.id = id;
    this.iconAddress = iconAddress;
    this.name = name;
    this.difficulty = difficulty;
    this.type = type;
    this.description = description;
    this.rules = rules;
    this.goal = goal;
    this.minPlayers = minPlayers;
    this.maxPlayers = maxPlayers;
    this.filename = filename;
  }


  String getFilename() {
    return filename;
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
