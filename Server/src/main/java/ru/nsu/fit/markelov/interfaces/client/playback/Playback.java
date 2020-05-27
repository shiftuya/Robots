package ru.nsu.fit.markelov.interfaces.client.playback;

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
     * Returns a map <userName -> index in list of game objects>.
     *
     * Used for binding users with game objects.
     *
     * @return a map <userName -> index in list of game objects>.
     */
    Map<String, Integer> getRobots();

    /**
     * Returns a list of game object states.
     *
     * @return a list of game object states.
     */
    List<List<GameObjectState>> getGameObjectStates();

    /**
     * Returns camera.
     *
     * @return camera.
     */
    default Camera getCamera() {
        return new Camera() {};
    }

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
