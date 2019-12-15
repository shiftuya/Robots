package ru.nsu.fit.markelov;

public interface SiteAPI {
    String getListOfLobbies();
    String getLevels();
    String getSolutions(String userName);
    String joinLobby(String userName, int lobbyID);
    String createLobby(String userName, int levelID);
}
