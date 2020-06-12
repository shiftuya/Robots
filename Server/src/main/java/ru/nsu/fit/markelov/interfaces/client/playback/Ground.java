package ru.nsu.fit.markelov.interfaces.client.playback;

public interface Ground {
    /**
     * Returns ground width and depth.
     *
     * @return ground width and depth.
     */
    default int getSize() {
        return 2500;
    }

    /**
     * Returns ground color.
     *
     * @return ground color.
     */
    default int getColor() {
        return 0xffffff;
    }

    /**
     * Returns number of divisions across the grid.
     *
     * @return number of divisions across the grid.
     */
    default int getGridDivisions() {
        return 20;
    }

    /**
     * Returns color of the grid lines.
     *
     * @return color of the grid lines.
     */
    default int getGridColor() {
        return 0x000000;
    }

    /**
     * Returns color of the centerline.
     *
     * @return color of the centerline.
     */
    default int getGridCenterLineColor() {
        return 0x000000;
    }

    /**
     * Returns grid opacity.
     *
     * @return grid opacity.
     */
    default float getGridOpacity() {
        return 0.2f;
    }
}
