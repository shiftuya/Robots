package ru.nsu.fit.markelov.interfaces.client.playback;

public interface Camera {
    /**
     * Returns camera starting position.
     *
     * @return camera starting position.
     */
    default Vector3 getPosition() {
        return new Vector3() {
            @Override
            public float getX() {
                return 200;
            }

            @Override
            public float getY() {
                return 100;
            }

            @Override
            public float getZ() {
                return 200;
            }
        };
    }

    /**
     * Returns render distance.
     *
     * @return render distance.
     */
    default int getRenderDistance() {
        return 10000;
    }

    /**
     * Returns scroll speed.
     *
     * @return scroll speed.
     */
    default float getScrollSpeed() {
        return 20;
    }
}
