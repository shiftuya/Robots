package ru.nsu.fit.markelov.mainmanager;

import java.util.Date;
import ru.nsu.fit.markelov.interfaces.client.Level;

public class Level1 implements Level {
  private int id;
  private String iconAddress;
  private String name;
  private LevelDifficulty difficulty;
  private String type;
  private String description;
  private String rules;
  private String goal;
  private int minPlayers;
  private int maxPlayers;
  private String filename;
  private String language;
  private String code;

  Level1(int id, String iconAddress, String name, LevelDifficulty difficulty, String type,
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

  public Level1(int id, String iconAddress, String name, LevelDifficulty difficulty, String type,
      String description, String rules, String goal, int minPlayers, int maxPlayers,
      String language, String code) {
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
    this.language = language;
    this.code = code;
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
  public LevelDifficulty getDifficulty() {
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

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getLanguage() {
    return language;
  }


}
