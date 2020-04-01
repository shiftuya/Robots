package ru.nsu.fit.markelov.interfaces.client;

import java.util.Collection;
import java.util.Map;

public interface Playback {

    /**
     * Returns a map of static game objects to its constant position.
     *
     * @return a map of static game objects to its constant position.
     */
    Map<GameObject, Vector3> getStaticGameObjects();

    /**
     * Returns a map of dynamic objects to the list of its position at every step of simulation.
     *
     * @return a map of dynamic objects to the list of its position at every step of simulation.
     */
    Map<GameObject, Collection<Vector3>> getDynamicGameObjects();
}
