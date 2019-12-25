package ru.nsu.fit.markelov.interfaces;

//import com.sun.istack.internal.NotNull;

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
   // @NotNull
    String getIconAddress();

    /**
     * Returns the level's name.
     *
     * @return the level's name.
     */
  //  @NotNull
    String getName();

    /**
     * Returns the level's difficulty.
     *
     * @return the level's difficulty.
     */
 //   @NotNull
    String getDifficulty();

    /**
     * Returns the level's type.
     *
     * @return the level's type.
     */
   // @NotNull
    String getType();

    /**
     * Returns the level's description.
     *
     * @return the level's description.
     */
 //   @NotNull
    String getDescription();

    /**
     * Returns the level's rules.
     *
     * @return the level's rules.
     */
  //  @NotNull
    String getRules();

    /**
     * Returns the level's goal.
     *
     * @return the level's goal.
     */
  //  @NotNull
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
