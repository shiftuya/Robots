package ru.nsu.fit.markelov.interfaces.client;

public interface Level {
    /**
     * Returns the unique id of the level.
     *
     * @return the unique id of the level.
     */
    int getId();

    /**
     * Returns the address of the level's icon.
     *
     * @return the address of the level's icon.
     */
    String getIconAddress();

    /**
     * Returns the level's name.
     *
     * @return the level's name.
     */
    String getName();

    /**
     * Returns the level's difficulty.
     *
     * @return the level's difficulty.
     */
    String getDifficulty();

    /**
     * Returns the level's type.
     *
     * @return the level's type.
     */
    String getType();

    /**
     * Returns the level's description.
     *
     * @return the level's description.
     */
    String getDescription();

    /**
     * Returns the level's rules.
     *
     * @return the level's rules.
     */
    String getRules();

    /**
     * Returns the level's goal.
     *
     * @return the level's goal.
     */
    String getGoal();

    /**
     * Returns minimal level's amount of players.
     *
     * @return minimal level's amount of players.
     */
    int getMinPlayers();

    /**
     * Returns maximal level's amount of players.
     *
     * @return maximal level's amount of players.
     */
    int getMaxPlayers();
}
