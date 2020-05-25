package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.Pair;
import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;

import java.util.List;
import java.util.Map;

public class Playback1 implements Playback {

  @Override
  public List<Pair<String, Integer>> getUserBindingWithObjects() { // TODO
    return null;
  }

  private int framesCount;
  private List<List<GameObjectState>> gameObjectStates;

  public Playback1(
      int framesCount, List<List<GameObjectState>> gameObjectStates, Map<String, Integer> robots) {
    this.framesCount = framesCount;
    this.gameObjectStates = gameObjectStates;
  }

  @Override
  public int getFramesCount() {
    return framesCount;
  }

  @Override
  public List<List<GameObjectState>> getGameObjectStates() {
    return gameObjectStates;
  }
}
