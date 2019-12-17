package ru.nsu.fit.markelov.managers;

import ru.nsu.fit.markelov.objects.*;

import java.util.ArrayList;
import java.util.List;

public class MainManagerClass implements MainManager {

    private LobbyManager lobbyManager;
    private LevelManager levelManager;
    private UserManager userManager;

    public MainManagerClass() {
        lobbyManager = new LobbyManagerClass();
        levelManager = new LevelManagerClass();
        userManager = new UserManagerClass();

        // ----- players -----

        PlayerClass player_1 = new PlayerClass();
        player_1.setAvatarAddress("/images/person-icon.png");
        player_1.setName("Vasily");
        player_1.setSubmitted(true);

        PlayerClass player_2 = new PlayerClass();
        player_2.setAvatarAddress("/images/person-icon.png");
        player_2.setName("Simon");
        player_2.setSubmitted(true);

        PlayerClass player_3 = new PlayerClass();
        player_3.setAvatarAddress("/images/person-icon.png");
        player_3.setName("Ivan");
        player_3.setSubmitted(false);

        PlayerClass player_4 = new PlayerClass();
        player_4.setAvatarAddress("/images/person-icon.png");
        player_4.setName("Oleg");
        player_4.setSubmitted(false);

        // ----- levels -----

        LevelClass level_1 = new LevelClass();
        level_1.setId(1);
        level_1.setIconAddress("/images/labyrinth-icon.png");
        level_1.setName("Labyrinth");
        level_1.setDifficulty("Easy");
        level_1.setType("Single");
        level_1.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...");
        level_1.setRules("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        level_1.setGoal("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        level_1.setMinPlayers(1);
        level_1.setMaxPlayers(1);
        levelManager.addLevelHARDCODED(level_1);

        LevelClass level_2 = new LevelClass();
        level_2.setId(2);
        level_2.setIconAddress("/images/vacuum-cleaner-icon.png");
        level_2.setName("Vacuum Cleaner");
        level_2.setDifficulty("Medium");
        level_2.setType("Multiplayer (2-4)");
        level_2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...");
        level_2.setRules("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        level_2.setGoal("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        level_2.setMinPlayers(2);
        level_2.setMaxPlayers(4);
        levelManager.addLevelHARDCODED(level_2);

        LevelClass level_3 = new LevelClass();
        level_3.setId(3);
        level_3.setIconAddress("/images/robot-wars-icon.png");
        level_3.setName("Robot Wars");
        level_3.setDifficulty("Hard");
        level_3.setType("Multiplayer (2)");
        level_3.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...");
        level_3.setRules("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        level_3.setGoal("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        level_3.setMinPlayers(2);
        level_3.setMaxPlayers(2);
        levelManager.addLevelHARDCODED(level_3);

        // ----- lobbies -----

        List<Player> players_for_lobby_1 = new ArrayList<>();
        players_for_lobby_1.add(player_2);
        LobbyClass lobby_1 = new LobbyClass();
        lobby_1.setId(1);
        lobby_1.setHostAvatarAddress("/images/person-icon.png");
        lobby_1.setHostName("Simon");
        lobby_1.setLevel(level_3);
        lobby_1.setCurrentPlayersAmount(1);
        lobby_1.setAcceptablePlayersAmount(2);
        lobby_1.setPlayers(players_for_lobby_1);
        lobbyManager.addLobbyHARDCODED(lobby_1);

        List<Player> players_for_lobby_2 = new ArrayList<>();
        players_for_lobby_2.add(player_1);
        players_for_lobby_2.add(player_2);
        players_for_lobby_2.add(player_4);
        LobbyClass lobby_2 = new LobbyClass();
        lobby_2.setId(2);
        lobby_2.setHostAvatarAddress("/images/person-icon.png");
        lobby_2.setHostName("Vasily");
        lobby_2.setLevel(level_2);
        lobby_2.setCurrentPlayersAmount(3);
        lobby_2.setAcceptablePlayersAmount(4);
        lobby_2.setPlayers(players_for_lobby_2);
        lobbyManager.addLobbyHARDCODED(lobby_2);

        List<Player> players_for_lobby_3 = new ArrayList<>();
        players_for_lobby_3.add(player_4);
        LobbyClass lobby_3 = new LobbyClass();
        lobby_3.setId(3);
        lobby_3.setHostAvatarAddress("/images/person-icon.png");
        lobby_3.setHostName("Ivan");
        lobby_3.setLevel(level_3);
        lobby_3.setCurrentPlayersAmount(1);
        lobby_3.setAcceptablePlayersAmount(2);
        lobby_3.setPlayers(players_for_lobby_3);
        lobbyManager.addLobbyHARDCODED(lobby_3);

        // ----- Solutions -----

        AttemptClass attempt_1_for_solution_1 = new AttemptClass();
        attempt_1_for_solution_1.setId(1);
        attempt_1_for_solution_1.setDate("03.11.2019");
        attempt_1_for_solution_1.setSuccessed(false);

        AttemptClass attempt_2_for_solution_1 = new AttemptClass();
        attempt_2_for_solution_1.setId(2);
        attempt_2_for_solution_1.setDate("05.11.2019");
        attempt_2_for_solution_1.setSuccessed(true);

        List<Attempt> attempts_for_solution_1 = new ArrayList<>();
        attempts_for_solution_1.add(attempt_1_for_solution_1);
        attempts_for_solution_1.add(attempt_2_for_solution_1);
        // -----------------------------------------------------------------------------------------
        AttemptClass attempt_1_for_solution_2 = new AttemptClass();
        attempt_1_for_solution_2.setId(1);
        attempt_1_for_solution_2.setDate("03.11.2019");
        attempt_1_for_solution_2.setSuccessed(false);

        AttemptClass attempt_2_for_solution_2 = new AttemptClass();
        attempt_2_for_solution_2.setId(2);
        attempt_2_for_solution_2.setDate("05.11.2019");
        attempt_2_for_solution_2.setSuccessed(false);

        AttemptClass attempt_3_for_solution_2 = new AttemptClass();
        attempt_3_for_solution_2.setId(3);
        attempt_3_for_solution_2.setDate("06.11.2019");
        attempt_3_for_solution_2.setSuccessed(false);

        List<Attempt> attempts_for_solution_2 = new ArrayList<>();
        attempts_for_solution_2.add(attempt_1_for_solution_2);
        attempts_for_solution_2.add(attempt_2_for_solution_2);
        attempts_for_solution_2.add(attempt_3_for_solution_2);
        // -----------------------------------------------------------------------------------------
        SolutionClass solution_1 = new SolutionClass();
        solution_1.setLevel(level_1);
        solution_1.setAttempts(attempts_for_solution_1);
        userManager.addSolutionHARDCODED(solution_1);

        SolutionClass solution_2 = new SolutionClass();
        solution_2.setLevel(level_2);
        solution_2.setAttempts(attempts_for_solution_2);
        userManager.addSolutionHARDCODED(solution_2);

        SolutionClass solution_3 = new SolutionClass();
        solution_3.setLevel(level_3);
        userManager.addSolutionHARDCODED(solution_3);
    }

    @Override
    public List<Lobby> getLobbies() {
        return lobbyManager.getLobbies();
    }

    @Override
    public List<Level> getLevels() {
        return levelManager.getLevels();
    }

    @Override
    public List<Solution> getSolutions(String userName) {
        return userManager.getSolutions(userName);
    }

    @Override
    public Lobby joinLobby(String userName, int lobbyID) {
        return lobbyManager.joinLobby(userName, lobbyID);
    }

    @Override
    public Lobby createLobby(String userName, int levelID, int playersAmount) {
        return lobbyManager.createLobby(userName, levelID, playersAmount);
    }

    @Override
    public List<Lobby> leaveLobby(String userName, int lobbyID) {
        return lobbyManager.leaveLobby(userName, lobbyID);
    }
}
