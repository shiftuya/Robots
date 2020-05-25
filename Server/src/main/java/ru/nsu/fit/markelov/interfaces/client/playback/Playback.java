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
     * Used for centering the camera on a robot of the user.
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
     * Returns ground.
     *
     * @return ground.
     */
    default Ground getGround() {
        return new Ground() {};
    }
}
