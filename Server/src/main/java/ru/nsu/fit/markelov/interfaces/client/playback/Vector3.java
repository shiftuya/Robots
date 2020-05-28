package ru.nsu.fit.markelov.interfaces.client.playback;

public interface Vector3 {
    /**
     * Returns x coordinate.
     *
     * @return x coordinate.
     */
    default float getX() {
        return 0;
    }

    /**
     * Returns y coordinate.
     *
     * @return y coordinate.
     */
    default float getY() {
        return 0;
    }

    /**
     * Returns z coordinate.
     *
     * @return z coordinate.
     */
    default float getZ() {
        return 0;
    }
}
