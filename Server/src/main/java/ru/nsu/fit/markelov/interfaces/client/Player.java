package ru.nsu.fit.markelov.interfaces.client;

public interface Player {
    /**
     * Returns the address of player's avatar icon.
     *
     * @return the address of player's avatar icon.
     */
    String getAvatarAddress();

    /**
     * Returns the player's unique name.
     *
     * @return the player's unique name.
     */
    String getName();

    /**
     * Returns whether the player has submitted his solution.
     *
     * @return whether the player has submitted his solution.
     */
    boolean isSubmitted();
}
