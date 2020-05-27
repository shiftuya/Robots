package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;

import java.util.List;
import java.util.Map;

public class Playback1 implements Playback {
  private int framesCount;
  private List<List<GameObjectState>> gameObjectStates;
  private Map<String, Integer> robots;

  public Playback1(
    int framesCount, List<List<GameObjectState>> gameObjectStates, Map<String, Integer> robots) {
    this.framesCount = framesCount;
    this.gameObjectStates = gameObjectStates;
    this.robots = robots;
  }

  @Override
  public int getFramesCount() {
    return framesCount;
  }

  @Override
  public Map<String, Integer> getRobots() {
    return robots;
  }

  @Override
  public List<List<GameObjectState>> getGameObjectStates() {
    return gameObjectStates;
  }

  public void setRobots(Map<String, Integer> robots) {
    this.robots = robots;
  }
}
