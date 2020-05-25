package ru.nsu.fit.markelov.interfaces.client.playback;

import ru.nsu.fit.markelov.interfaces.client.Pair;

import java.util.List;
import java.util.Map;

public interface Playback {
    /**
     * Returns a number of frames.
     *
     * @return a number of frames.
     */
    int getFramesCount();

    /**
     * Returns a collection of users with their indexes in list of game objects.
     *
     * Used for binding users with game objects.
     *
     * @return a collection of users with their indexes in list of game objects.
     */
    List<Pair<String, Integer>> getUserBindingWithObjects();

    /**
     * Returns a list of game object states.
     *
     * @return a list of game object states.
     */
    List<List<GameObjectState>> getGameObjectStates();

    /**
     * Returns background color.
     *
     * @return background color.
     */
    default int getBackgroundColor() {
        return 0xa0a0a0;
    }

    /**
     * Returns ground.
     *
     * @return ground.
     */
    default Ground getGround() {
        return new Ground() {};
    }
}
