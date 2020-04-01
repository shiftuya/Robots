package ru.nsu.fit.markelov.interfaces.client;

import java.util.Collection;

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
     * Returns collection of available lobbies.
     *
     * The collection of lobbies must be sorted by date of creation (the newest one - in the head).
     *
     * @return collection of available lobbies.
     */
    Collection<Lobby> getLobbies();

    /**
     * Returns collection of required users.
     *
     * If user (gotten by 'userName') is a teacher - collection of students must be returned,
     * If user (gotten by 'userName') is an admin - collection of all users must be returned.
     *
     * A collection must be sorted by user names.
     *
     * @param userName user unique name.
     * @return collection of required users.
     */
    Collection<User> getUsers(String userName);

    /**
     * Returns a collection of created levels.
     *
     * @return a collection of created levels.
     */
    Collection<Level> getLevels();

    /**
     * Places a user in the lobby gotten by specified 'lobbyID'.
     *
     * A host-user must be in the head of the user list.
     *
     * @param userName unique user name.
     * @param lobbyID  unique lobby id.
     * @return lobby which user was placed in.
     */
    Lobby joinLobby(String userName, int lobbyID);

    /**
     * Creates a new lobby by 'levelID' and places a user in it.
     *
     * A host-user must be in the head of the user list.
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
     * A host-user must be in the head of the user list.
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
     * @param username unique user name.
     * @param code     a code to compile.
     * @param lobbyId  unique lobby id.
     * @return the result of compilation.
     */
    CompileResult submit(String username, int lobbyId, String code);

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
    Collection<SimulationResult> getUserSimulationResultsOnLevel(String username, int levelId);

    /**
     * Creates a user and informs whether it is successfully created.
     *
     * @param userName      unique user name.
     * @param password      user password.
     * @param type          user type.
     * @param avatarAddress user avatar address.
     * @return whether user is successfully created.
     */
    boolean createUser(String userName, String password, User.UserType type, String avatarAddress);

    /**
     * Updates a user and informs whether it is successfully updated.
     *
     * If 'password', 'type' or 'avatarAddress' is null, then corresponding user field stays the
     * same.
     *
     * @param userName      unique user name.
     * @param password      user password.
     * @param type          user type.
     * @param avatarAddress user avatar address.
     * @return whether user is successfully created.
     */
    boolean updateUser(String userName, String password, User.UserType type, String avatarAddress);

    /**
     * Blocks/unblocks user and informs whether it is successfully blocked/unblocked.
     *
     * @param userName unique user name.
     * @param block    true to for blocking and false for unblocking.
     * @return whether user is successfully blocked/unblocked.
     */
    boolean blockUser(String userName, boolean block);

    /**
     * Deletes user and informs whether it is successfully deleted.
     *
     * @param userName unique user name.
     * @return whether user is successfully deleted.
     */
    boolean removeUser(String userName);

    /**
     * Creates/edits a level and informs whether it is successfully created/edited.
     *
     * Id levelID is null - a new level must be created;
     *          otherwise - an existing level (gotten by this id) must be edited.
     *
     * If level is being created:
     *     - if iconResource is null, default icon must be used.
     *
     * If level is being edited:
     *     - if iconResource is null, previously saved icon must be used.
     *     - if levelResources is null, previously saved level resources must be used.
     *     - if levelResources is not null, new resources must be added to previously saved ones.
     *
     * @param levelID        unique level id.
     * @param name           level name.
     * @param difficulty     level difficulty.
     * @param minPlayers     level minimal players count.
     * @param maxPlayers     level maximal players count.
     * @param iconResource   level icon.
     * @param description    level description.
     * @param rules          level rules.
     * @param goal           level goal.
     * @param levelResources level extra resources.
     * @param code           level code.
     * @param language       level language.
     * @return whether a level is successfully created/edited.
     */
    boolean submitLevel(Integer levelID, String name, String difficulty,
                        Integer minPlayers, Integer maxPlayers, Resource iconResource,
                        String description, String rules, String goal,
                        Collection<Resource> levelResources, String code, String language);

    /**
     * Returns a level gotten by specified 'levelID'.
     *
     * @param levelID unique level id.
     * @return a level.
     */
    Level getLevel(int levelID);

    /**
     * Deletes a level and informs whether it is successfully deleted.
     *
     * @param levelID unique level id.
     * @return whether a level is successfully deleted.
     */
    boolean deleteLevel(int levelID);

    /**
     * Returns a collection of simulator units url.
     *
     * @return a collection of simulator units url.
     */
    Collection<String> getSimulators();

    /**
     * Adds new simulator unit and informs whether it is successfully added.
     *
     * @param url location of new simulator unit.
     * @return whether simulator unit is successfully added.
     */
    boolean addSimulator(String url);

    /**
     * Deletes new simulator unit and informs whether it is successfully deleted.
     *
     * @param url location of simulator unit to remove.
     * @return whether simulator unit is successfully deleted.
     */
    boolean removeSimulator(String url);
}
