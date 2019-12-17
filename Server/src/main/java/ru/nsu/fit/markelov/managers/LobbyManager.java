package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.Lobby;

import java.util.List;

public interface LobbyManager {

    /**
     * Returns list of available lobbies.
     *
     * @return list of available lobbies.
     */
    List<Lobby> getLobbies();

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



    // ----- (for HARDCODING only?) -----

    /**
     * Adds lobby to the list of lobbies.
     *
     * @param lobby a lobby to add.
     */
    void addLobbyHARDCODED(Lobby lobby);
}
