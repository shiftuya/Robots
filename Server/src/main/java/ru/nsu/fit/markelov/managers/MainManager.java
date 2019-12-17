package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Level;
import ru.nsu.fit.markelov.objects.Lobby;
import ru.nsu.fit.markelov.objects.Solution;

import java.util.List;

public interface MainManager {

    /**
     * Returns list of available lobbies.
     *
     * @return list of available lobbies.
     */
    List<Lobby> getLobbies();

    /**
     * Returns list of created levels.
     *
     * @return list of created levels.
     */
    List<Level> getLevels();

    /**
     * Returns list of all the solutions of specified user.
     *
     * @param userName user's unique name.
     * @return         list of all the solutions of specified user.
     */
    List<Solution> getSolutions(String userName);

    /**
     * Places a user in the lobby gotten by specified 'lobbyID'.
     *
     * @param userName user's unique name.
     * @param lobbyID  lobby's unique id.
     * @return         lobby which user was placed in.
     */
    Lobby joinLobby(String userName, int lobbyID);

    /**
     * Creates a new lobby by 'levelID' and places a user in it.
     *
     * @param userName      user's unique name.
     * @param levelID       level's unique id.
     * @param playersAmount amount of players.
     * @return              the created lobby.
     */
    Lobby createLobby(String userName, int levelID, int playersAmount);

    /**
     * Removes a user from the lobby gotten by specified 'lobbyID'.
     *
     * @param userName user's unique name.
     * @param lobbyID  lobby's unique id.
     * @return         list of available lobbies.
     */
    List<Lobby> leaveLobby(String userName, int lobbyID);
}
