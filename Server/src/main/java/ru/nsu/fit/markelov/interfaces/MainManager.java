package ru.nsu.fit.markelov.interfaces;

//import com.sun.istack.internal.NotNull;

import java.util.List;

public interface MainManager {

    /**
     * Logs a user in the system if his username exists in the database.
     *
     * @param username user's unique name.
     * @return         whether the user is found in the database and successfully logged in.
     */
    boolean login(/*@NotNull*/ String username/*, @NotNull String password*/);

    /**
     * Logs a user out of the system if his username exists in the database.
     *
     * @param username user's unique name.
     * @return         whether the user is found in the database and successfully logged out.
     */
    boolean logout(/*@NotNull*/ String username);

    /**
     * Returns list of available lobbies.
     *
     * The list of lobbies must be sorted by date of creation (the newest one - in the head).
     *
     * @return list of available lobbies.
     */
    //@NotNull
    List<Lobby> getLobbies();

    /**
     * Returns list of created levels.
     *
     * @return list of created levels.
     */
  //  @NotNull
    List<Level> getLevels();

    /**
     * Returns list of all the solutions of specified user.
     *
     * @param userName user's unique name.
     * @return         list of all the solutions of specified user.
     */
   // @NotNull
    List<Solution> getSolutions(/*@NotNull*/ String userName);

    /**
     * Places a user in the lobby gotten by specified 'lobbyID'.
     *
     * A host-user must be in the head of the list.
     *
     * @param userName user's unique name.
     * @param lobbyID  lobby's unique id.
     * @return         lobby which user was placed in.
     */
   // @NotNull
    Lobby joinLobby(/*@NotNull*/ String userName, int lobbyID);

    /**
     * Creates a new lobby by 'levelID' and places a user in it.
     *
     * A host-user must be in the head of the list.
     *
     * @param userName      user's unique name.
     * @param levelID       level's unique id.
     * @param playersAmount amount of players.
     * @return              the created lobby.
     */
    /*@NotNull*/
    Lobby createLobby(/*@NotNull*/ String userName, int levelID, int playersAmount);

    /**
     * Removes a user from the lobby gotten by specified 'lobbyID'.
     *
     * @param userName user's unique name.
     * @param lobbyID  lobby's unique id.
     * @return         whether the user has been successfully removed from the lobby.
     */
    boolean leaveLobby(/*@NotNull*/ String userName, int lobbyID);

    /**
     * Returns the lobby gotten by specified 'lobbyID'.
     *
     * @param userName user's unique name.
     * @param lobbyID  lobby's unique id.
     * @return         the lobby.
     */
    /*@NotNull*/
    Lobby getLobby(/*@NotNull*/ String userName, int lobbyID);

    /**
     * Compiles the specified code and returns compile result.
     * <p>
     * If the compilation is successful, the code is being saved for the future simulation.
     * <p>
     * (A simulation itself starts automatically when all the users successfully submitted the code.
     *
     * @param lobbyId  lobby's unique id.
     * @param username user's unique name.
     * @param code     a code to compile.
     * @return         the result of compilation.
     */
    /*@NotNull*/
    CompileResult submit(/*@NotNull*/ String username, /*@NotNull*/ String code, int lobbyId);

    /**
     * Cancels the submission of the lately compiled code and returns the code itself. In case the
     * the user hasn't submitted any code yet, returns null.
     *
     * @param username user's unique name.
     * @param lobbyId  whether the submission was successfully cancelled.
     * @return         the earlier submitted code.
     */
    String editSubmittedCode(/*@NotNull*/ String username, int lobbyId);

    /**
     * Returns the simulation result or null in case it hasn't been processed yet.
     *
     * @param username user's unique name.
     * @param lobbyId  lobby's unique id.
     * @return         simulation result or null in case it hasn't been processed yet.
     */
    SimulationResult getSimulationResult(/*@NotNull*/ String username, int lobbyId);
}
