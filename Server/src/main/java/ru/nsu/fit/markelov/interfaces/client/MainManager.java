package ru.nsu.fit.markelov.interfaces.client;

public interface MainManager {
    /**
     * Returns unique user name gotten by specified 'token'.
     *
     * @param token user unique token.
     * @return unique user name.
     */
    String getUserName(String token);

    /**
     * Generates user unique token in case a pair [userName/password] exists in the database.
     *
     * @param userName unique user name.
     * @param password user password.
     * @return generated user unique token.
     */
    String login(String userName, String password);

    /**
     * Removes the token from the database.
     *
     * @param token user unique token.
     */
    void logout(String token);

    /**
     * Returns collection of available lobbies.
     *
     * The collection of lobbies must be sorted by date of creation (the newest one - in the head).
     *
     * @param token user unique token.
     * @return collection of available lobbies.
     */
    Iterable<Lobby> getLobbies(String token);

    /**
     * Returns collection of required users.
     *
     * If user (gotten by specified 'token') is a teacher - collection of students must be returned.
     * If user (gotten by specified 'token') is an admin - collection of all users must be returned.
     *
     * A collection must be sorted by user names.
     *
     * @param token user unique token.
     * @return collection of required users.
     */
    Iterable<User> getUsers(String token);

    /**
     * Returns a collection of created levels.
     *
     * @param token user unique token.
     * @return a collection of created levels.
     */
    Iterable<Level> getLevels(String token);

    /**
     * Places a user in the lobby gotten by specified 'lobbyId'.
     *
     * A host-user must be in the head of the user list.
     *
     * @param token   unique user token.
     * @param lobbyId unique lobby id.
     * @return lobby which user was placed in.
     */
    Lobby joinLobby(String token, int lobbyId);

    /**
     * Creates a new lobby by 'levelId' and places a user in it.
     *
     * A host-user must be in the head of the user list.
     *
     * @param token         unique user token.
     * @param levelId       unique level id.
     * @param playersAmount amount of players.
     * @return the created lobby.
     */
    Lobby createLobby(String token, int levelId, int playersAmount);

    /**
     * Removes a user from the lobby gotten by specified 'lobbyId'.
     *
     * @param token   unique user token.
     * @param lobbyId unique lobby id.
     */
    void leaveLobby(String token, int lobbyId);

    /**
     * Returns the lobby gotten by specified 'lobbyId'.
     *
     * A host-user must be in the head of the user list.
     *
     * @param token   unique user token.
     * @param lobbyId unique lobby id.
     * @return the lobby.
     */
    Lobby returnToLobby(String token, int lobbyId);

    /**
     * Compiles the specified code and returns compile result.
     *
     * If the compilation is successful, the code is being saved for the future simulation.
     *
     * (A simulation itself starts automatically when all the users successfully submitted the code).
     *
     * @param token   unique user token.
     * @param code    a code to compile.
     * @param lobbyId unique lobby id.
     * @return the result of compilation.
     */
    CompileResult submit(String token, int lobbyId, String code);

    /**
     * Cancels the submission of the lately compiled code and returns the code itself. In case the
     * user hasn't submitted any code yet, returns null.
     *
     * @param token   unique user token.
     * @param lobbyId whether the submission was successfully cancelled.
     * @return the earlier submitted code.
     */
    String editSubmittedCode(String token, int lobbyId);

    /**
     * Returns whether the simulation has already been finished.
     *
     * @param token   unique user token.
     * @param lobbyId unique lobby id.
     * @return whether the simulation has already been finished.
     */
    boolean isSimulationFinished(String token, int lobbyId);

    /**
     * Returns the simulation result or null in case it hasn't been processed yet.
     *
     * @param token   unique user token.
     * @param lobbyId unique lobby id.
     * @return simulation result or null in case it hasn't been processed yet.
     */
    SimulationResult getSimulationResult(String token, int lobbyId);

    /**
     * Returns user simulation log.
     *
     * @param token              unique user token.
     * @param userName           unique user name.
     * @param simulationResultId unique simulation result id.
     * @return user simulation log.
     */
    String getLog(String token, String userName, int simulationResultId);

    /**
     * Returns user script for his robot.
     *
     * @param token              unique user token.
     * @param userName           unique user name.
     * @param simulationResultId unique simulation result id.
     * @return user script for his robot.
     */
    String getScript(String token, String userName, int simulationResultId);

    /**
     * Returns all the user simulation results on specified level.
     *
     * @param token    unique user token.
     * @param userName unique user name.
     * @param levelId  unique level id.
     * @return all the user simulation results on specified level.
     */
    Iterable<SimulationResult> getUserSimulationResultsOnLevel(String token, String userName, int levelId);

    /**
     * Returns a user gotten by specified 'userName'.
     *
     * @param token    unique user token.
     * @param userName unique user name.
     * @return a user.
     */
    User getUser(String token, String userName);

    /**
     * Creates/edits a user.
     *
     * If 'create' is true - a new user must be created;
     *           otherwise - an existing user (gotten by specified 'userName') must be edited.
     *
     * If user is being created:
     *     - if 'avatarResource' is null, default avatar must be used.
     *
     * If user is being edited:
     *     - if 'avatarResource' is null, previously saved avatar must be used.
     *
     * @param token          unique user token.
     * @param create         true for creating and false for editing.
     * @param userName       unique user name.
     * @param password       user password.
     * @param type           user type.
     * @param avatarResource user avatar resource.
     */
    void submitUser(String token, boolean create, String userName,
                    String password, String type, Resource avatarResource);

    /**
     * Blocks/unblocks user.
     *
     * @param token    unique user token.
     * @param userName unique user name.
     * @param block    true for blocking and false for unblocking.
     */
    void blockUser(String token, String userName, boolean block);

    /**
     * Deletes user.
     *
     * @param token    unique user token.
     * @param userName unique user name.
     */
    void removeUser(String token, String userName);

    /**
     * Creates/edits a level.
     *
     * Id 'create' is true - a new level must be created;
     *           otherwise - an existing level (gotten by specified 'levelId') must be edited.
     *
     * If level is being created:
     *     - if iconResource is null, default icon must be used.
     *
     * If level is being edited:
     *     - if 'iconResource' is null, previously saved icon must be used.
     *     - if 'levelResources' is null, previously saved level resources must be used.
     *     - if 'levelResources' is not null, new resources must be added to previously saved ones.
     *
     * @param token          unique user token.
     * @param create         true for creating and false for editing.
     * @param levelId        unique level id.
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
     */
    void submitLevel(String token, boolean create, Integer levelId, String name,
                     String difficulty, Integer minPlayers, Integer maxPlayers,
                     Resource iconResource, String description, String rules, String goal,
                     Iterable<Resource> levelResources, String code, String language);

    /**
     * Returns a level gotten by specified 'levelId'.
     *
     * @param token   unique user token.
     * @param levelId unique level id.
     * @return a level.
     */
    Level getLevel(String token, int levelId);

    /**
     * Deletes a level.
     *
     * @param token   unique user token.
     * @param levelId unique level id.
     */
    void deleteLevel(String token, int levelId);

    /**
     * Returns a collection of simulator units url.
     *
     * @param token unique user token.
     * @return a collection of simulator units url.
     */
    Iterable<String> getSimulators(String token);

    /**
     * Adds new simulator unit.
     *
     * @param token unique user token.
     * @param url   location of new simulator unit.
     */
    void addSimulator(String token, String url);

    /**
     * Deletes simulator unit.
     *
     * @param token unique user token.
     * @param url   location of simulator unit to remove.
     */
    void removeSimulator(String token, String url);
}
