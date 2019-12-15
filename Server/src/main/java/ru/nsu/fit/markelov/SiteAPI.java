package ru.nsu.fit.markelov;

public interface SiteAPI {
    String getListOfLobbies();
    String getLevels();
    String getSolutions();
    String getLobby(int lobbyID);
    String createLobby(int levelID);
}
