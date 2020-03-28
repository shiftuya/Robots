package ru.nsu.fit.markelov.interfaces.client;

public interface Level {
    /**
     * Returns the unique level id.
     *
     * @return the unique level id.
     */
    int getId();

    /**
     * Returns the level icon address.
     *
     * @return the level icon address.
     */
    String getIconAddress();

    /**
     * Returns the level name.
     *
     * @return the level name.
     */
    String getName();

    /**
     * Returns the level difficulty.
     *
     * @return the level difficulty.
     */
    String getDifficulty();

    /**
     * Returns the level type.
     *
     * @return the level type.
     */
    String getType();

    /**
     * Returns the level description.
     *
     * @return the level description.
     */
    String getDescription();

    /**
     * Returns the level rules.
     *
     * @return the level rules.
     */
    String getRules();

    /**
     * Returns the level goal.
     *
     * @return the level goal.
     */
    String getGoal();

    /**
     * Returns minimal level amount of players.
     *
     * @return minimal level amount of players.
     */
    int getMinPlayers();

    /**
     * Returns maximal level amount of players.
     *
     * @return maximal level amount of players.
     */
    int getMaxPlayers();

    /**
     * Returns whether a level is active (available to play).
     *
     * @return whether a level is active (available to play).
     */
    boolean isActive();
}
