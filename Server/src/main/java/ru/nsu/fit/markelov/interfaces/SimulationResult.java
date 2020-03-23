package ru.nsu.fit.markelov.interfaces;

import ru.nsu.fit.markelov.interfaces.playback.Playback;

import java.util.Date;

public interface SimulationResult {
  /**
   * Returns the unique id of simulation result.
   *
   * @return the unique id of simulation result.
   */
  int getId();

  /**
   * Returns whether the user robot has reached the goal.
   *
   * @param username user unique name.
   * @return whether the user robot has reached the goal.
   */
  boolean isSuccessful(String username);

  /**
   * Returns the date of simulation result.
   *
   * @return the date of simulation result.
   */
  Date getDate();

  /**
   * Returns user simulation log.
   *
   * @param username user unique name.
   * @return user simulation log.
   */
  String getLog(String username);

  /**
   * Get playback for specified user.
   *
   * @param username user unique name.
   * @return playback of simulation.
   */
  Playback getPlayback(String username);
}
