package ru.nsu.fit.markelov.interfaces.client;

public interface Level {

    enum LevelDifficulty { Easy, Medium, Hard }

    /**
     * Returns unique level id.
     *
     * @return unique level id.
     */
    int getId();

    /**
     * Returns level icon address.
     *
     * @return level icon address.
     */
    String getIconAddress();

    /**
     * Returns level name.
     *
     * @return level name.
     */
    String getName();

    /**
     * Returns level difficulty.
     *
     * @return level difficulty.
     */
    LevelDifficulty getDifficulty();

    /**
     * Returns level type.
     *
     * @return level type.
     */
    String getType();

    /**
     * Returns level description.
     *
     * @return level description.
     */
    String getDescription();

    /**
     * Returns level rules.
     *
     * @return level rules.
     */
    String getRules();

    /**
     * Returns level goal.
     *
     * @return level goal.
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
     * Returns level code.
     *
     * @return level code.
     */
    String getCode();

    /**
     * Returns level language.
     *
     * @return level language.
     */
    String getLanguage();
}
