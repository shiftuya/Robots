package ru.nsu.fit.markelov.interfaces.client;

public interface Player {
    /**
     * Returns the player avatar address.
     *
     * @return the player avatar address.
     */
    String getAvatarAddress();

    /**
     * Returns the unique player name.
     *
     * @return the unique player name.
     */
    String getName();

    /**
     * Returns whether the player has submitted his solution.
     *
     * @return whether the player has submitted his solution.
     */
    boolean isSubmitted();
}
