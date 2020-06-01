package ru.nsu.fit.markelov.interfaces.client.playback;

import java.util.Map;
import java.util.TreeMap;

public interface GameObjectState {
    /**
     * Returns the starting frame number of this state.
     *
     * @return the starting frame number of this state.
     */
    int getStartingFrame();

    /**
     * Returns the ending frame number of this state.
     *
     * @return the ending frame number of this state.
     */
    int getEndingFrame();

    /**
     * Returns the position of this object.
     *
     * @return the position of this object.
     */
    default Vector3 getPosition() {
        return new Vector3() {};
    }

    /**
     * Returns the dimension of this object.
     *
     * @return the dimension of this object.
     */
    default Vector3 getDimension() {
        return new Vector3() {
            @Override
            public float getX() {
                return 50;
            }

            @Override
            public float getY() {
                return 50;
            }

            @Override
            public float getZ() {
                return 50;
            }
        };
    }

    /**
     * Returns the rotation of this object.
     *
     * @return the rotation of this object.
     */
    default Vector3 getRotation() {
        return new Vector3() {};
    }

    /**
     * Returns the color of this object.
     *
     * @return the color of this object.
     */
    default int getColor() {
        return 0x000000;
    }

    /**
     * Returns a collection of sensors with their values.
     *
     * @return a collection of sensors with their values.
     */
    default Map<String, String> getSensorValues() {
        return new TreeMap<>();
    }
}
