package ru.nsu.fit.markelov.interfaces.client.playback;

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
    Vector3 getPosition();

    /**
     * Returns the dimension of this object.
     *
     * @return the dimension of this object.
     */
    Vector3 getDimension();

    /**
     * Returns the rotation of this object.
     *
     * @return the rotation of this object.
     */
    Vector3 getRotation();

    /**
     * Returns the color of this object.
     *
     * @return the color of this object.
     */
    int getColor();
}
