package ru.nsu.fit.markelov.interfaces.client;

import java.util.Date;

public interface SimulationResult {
  /**
   * Returns the unique simulation result id.
   *
   * @return the unique simulation result id.
   */
  int getId();

  /**
   * Returns whether the user robot has reached the goal.
   *
   * @param username unique user name.
   * @return whether the user robot has reached the goal.
   */
  boolean isSuccessful(String username);

  /**
   * Returns the simulation result date.
   *
   * @return the simulation result date.
   */
  Date getDate();

  /**
   * Returns user simulation log.
   *
   * @param username unique user name.
   * @return user simulation log.
   */
  String getLog(String username);

  /**
   * Get playback for specified user.
   *
   * @param username unique user name.
   * @return playback of simulation.
   */
  Playback getPlayback(String username);
}
