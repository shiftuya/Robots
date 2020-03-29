package ru.nsu.fit.markelov.interfaces.client;

import java.util.List;

public interface MainManager {
    /**
     * Logs a user in the system if his username exists in the database.
     *
     * @param username unique user name.
     * @return whether the user is found in the database and successfully logged in.
     */
    boolean login(String username/*, String password*/);

    /**
     * Logs a user out of the system if his username exists in the database.
     *
     * @param username unique user name.
     * @return whether the user is found in the database and successfully logged out.
     */
    boolean logout(String username);

    /**
     * Returns list of available lobbies.
     *
     * The list of lobbies must be sorted by date of creation (the newest one - in the head).
     *
     * @return list of available lobbies.
     */
    List<Lobby> getLobbies();

    /**
     * Returns a list of created levels.
     *
     * @param onlyActive if true - return only active levels. If false - return all the levels.
     * @return a list of created levels.
     */
    List<Level> getLevels(boolean onlyActive);

    /**
     * Returns list of all the solutions of specified user.
     *
     * @param userName unique user name.
     * @return list of all the solutions of specified user.
     */
    //List<Solution> getSolutions(String userName);

    /**
     * Places a user in the lobby gotten by specified 'lobbyID'.
     *
     * A host-user must be in the head of the list.
     *
     * @param userName unique user name.
     * @param lobbyID  unique lobby id.
     * @return lobby which user was placed in.
     */
    Lobby joinLobby(String userName, int lobbyID);

    /**
     * Creates a new lobby by 'levelID' and places a user in it.
     *
     * A host-user must be in the head of the list.
     *
     * @param userName      unique user name.
     * @param levelID       unique level id.
     * @param playersAmount amount of players.
     * @return the created lobby.
     */
    Lobby createLobby(String userName, int levelID, int playersAmount);

    /**
     * Removes a user from the lobby gotten by specified 'lobbyID'.
     *
     * @param userName unique user name.
     * @param lobbyID  unique lobby id.
     * @return whether the user has been successfully removed from the lobby.
     */
    boolean leaveLobby(String userName, int lobbyID);

    /**
     * Returns the lobby gotten by specified 'lobbyID'.
     *
     * A host-user must be in the head of the list.
     *
     * @param userName unique user name.
     * @param lobbyID  unique lobby id.
     * @return the lobby.
     */
    Lobby returnToLobby(String userName, int lobbyID);

    /**
     * Compiles the specified code and returns compile result.
     *
     * If the compilation is successful, the code is being saved for the future simulation.
     *
     * (A simulation itself starts automatically when all the users successfully submitted the code)
     *
     * @param lobbyId  unique lobby id.
     * @param username unique user name.
     * @param code     a code to compile.
     * @return the result of compilation.
     */
    CompileResult submit(String username, String code, int lobbyId);

    /**
     * Cancels the submission of the lately compiled code and returns the code itself. In case the
     * user hasn't submitted any code yet, returns null.
     *
     * @param username unique user name.
     * @param lobbyId  whether the submission was successfully cancelled.
     * @return the earlier submitted code.
     */
    String editSubmittedCode(String username, int lobbyId);

    /**
     * Returns whether the simulation has already been finished.
     *
     * @param lobbyId unique lobby id.
     * @return whether the simulation has already been finished.
     */
    boolean isSimulationFinished(int lobbyId);

    /**
     * Returns the simulation result or null in case it hasn't been processed yet.
     *
     * @param username unique user name.
     * @param lobbyId  unique lobby id.
     * @return simulation result or null in case it hasn't been processed yet.
     */
    SimulationResult getSimulationResult(String username, int lobbyId);

    /**
     * Returns all the user simulation results on specified level.
     *
     * @param username unique user name.
     * @param levelId  unique level id.
     * @return all the user simulation results on specified level.
     */
    List<SimulationResult> getUserSimulationResultsOnLevel(String username, int levelId);

    /**
     * Creates a level and informs whether it is successfully created.
     *
     * @param name           level name.
     * @param difficulty     level difficulty.
     * @param players        level players.
     * @param iconResource   level icon.
     * @param description    level description.
     * @param rules          level rules.
     * @param goal           level goal.
     * @param levelResources level extra resources.
     * @param code           level code.
     * @return whether a level is successfully created.
     */
    boolean createLevel(String name, String difficulty, Integer players, Resource iconResource, String description,
                        String rules, String goal, List<Resource> levelResources, String code);
}
