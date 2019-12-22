package ru.nsu.fit.markelov.managers_hardcoded;

import ru.nsu.fit.markelov.interfaces.CompileResult;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Lobby;
import ru.nsu.fit.markelov.interfaces.MainManager;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.Solution;
import ru.nsu.fit.markelov.objects_hardcoded.LevelHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.LobbyHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.PlayerHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.SimulationResultHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.SolutionHardcoded;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainManagerHardcoded implements MainManager {

    private LobbyManagerHardcoded lobbyManager;
    private LevelManagerHardcoded levelManager;
    private UserManagerHardcoded userManager;

    public MainManagerHardcoded() {
        lobbyManager = new LobbyManagerHardcoded();
        levelManager = new LevelManagerHardcoded();
        userManager = new UserManagerHardcoded();

        // ----- players -----

        PlayerHardcoded player_1 = new PlayerHardcoded();
        player_1.setAvatarAddress("/images/person-icon.png");
        player_1.setName("Vasily");
        player_1.setSubmitted(true);

        PlayerHardcoded player_2 = new PlayerHardcoded();
        player_2.setAvatarAddress("/images/person-icon.png");
        player_2.setName("Simon");
        player_2.setSubmitted(true);

        PlayerHardcoded player_3 = new PlayerHardcoded();
        player_3.setAvatarAddress("/images/person-icon.png");
        player_3.setName("Ivan");
        player_3.setSubmitted(false);

        PlayerHardcoded player_4 = new PlayerHardcoded();
        player_4.setAvatarAddress("/images/person-icon.png");
        player_4.setName("Oleg");
        player_4.setSubmitted(false);

        // ----- levels -----

        LevelHardcoded level_1 = new LevelHardcoded();
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
        levelManager.addLevel(level_1);

        LevelHardcoded level_2 = new LevelHardcoded();
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
        levelManager.addLevel(level_2);

        LevelHardcoded level_3 = new LevelHardcoded();
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
        levelManager.addLevel(level_3);

        // ----- lobbies -----

        List<Player> players_for_lobby_1 = new ArrayList<>();
        players_for_lobby_1.add(player_2);
        LobbyHardcoded lobby_1 = new LobbyHardcoded();
        lobby_1.setId(1);
        lobby_1.setHostAvatarAddress("/images/person-icon.png");
        lobby_1.setHostName("Simon");
        lobby_1.setLevel(level_3);
        lobby_1.setCurrentPlayersAmount(1);
        lobby_1.setAcceptablePlayersAmount(2);
        lobby_1.setPlayers(players_for_lobby_1);
        lobbyManager.addLobby(lobby_1);

        List<Player> players_for_lobby_2 = new ArrayList<>();
        players_for_lobby_2.add(player_1);
        players_for_lobby_2.add(player_2);
        players_for_lobby_2.add(player_4);
        LobbyHardcoded lobby_2 = new LobbyHardcoded();
        lobby_2.setId(2);
        lobby_2.setHostAvatarAddress("/images/person-icon.png");
        lobby_2.setHostName("Vasily");
        lobby_2.setLevel(level_2);
        lobby_2.setCurrentPlayersAmount(3);
        lobby_2.setAcceptablePlayersAmount(4);
        lobby_2.setPlayers(players_for_lobby_2);
        lobbyManager.addLobby(lobby_2);

        List<Player> players_for_lobby_3 = new ArrayList<>();
        players_for_lobby_3.add(player_4);
        LobbyHardcoded lobby_3 = new LobbyHardcoded();
        lobby_3.setId(3);
        lobby_3.setHostAvatarAddress("/images/person-icon.png");
        lobby_3.setHostName("Ivan");
        lobby_3.setLevel(level_3);
        lobby_3.setCurrentPlayersAmount(1);
        lobby_3.setAcceptablePlayersAmount(2);
        lobby_3.setPlayers(players_for_lobby_3);
        lobbyManager.addLobby(lobby_3);

        // ----- Solutions -----

        SimulationResultHardcoded attempt_1_for_solution_1 = new SimulationResultHardcoded();
        attempt_1_for_solution_1.setId(1);
//        attempt_1_for_solution_1.setDate("03.11.2019");
        attempt_1_for_solution_1.setSuccessful(false);

        SimulationResultHardcoded attempt_2_for_solution_1 = new SimulationResultHardcoded();
        attempt_2_for_solution_1.setId(2);
//        attempt_2_for_solution_1.setDate("05.11.2019");
        attempt_2_for_solution_1.setSuccessful(true);

        List<SimulationResult> attempts_for_solution_1 = new ArrayList<>();
        attempts_for_solution_1.add(attempt_1_for_solution_1);
        attempts_for_solution_1.add(attempt_2_for_solution_1);
        // -----------------------------------------------------------------------------------------
        SimulationResultHardcoded attempt_1_for_solution_2 = new SimulationResultHardcoded();
        attempt_1_for_solution_2.setId(1);
//        attempt_1_for_solution_2.setDate("03.11.2019");
        attempt_1_for_solution_2.setSuccessful(false);

        SimulationResultHardcoded attempt_2_for_solution_2 = new SimulationResultHardcoded();
        attempt_2_for_solution_2.setId(2);
//        attempt_2_for_solution_2.setDate("05.11.2019");
        attempt_2_for_solution_2.setSuccessful(false);

        SimulationResultHardcoded attempt_3_for_solution_2 = new SimulationResultHardcoded();
        attempt_3_for_solution_2.setId(3);
//        attempt_3_for_solution_2.setDate("06.11.2019");
        attempt_3_for_solution_2.setSuccessful(false);

        List<SimulationResult> attempts_for_solution_2 = new ArrayList<>();
        attempts_for_solution_2.add(attempt_1_for_solution_2);
        attempts_for_solution_2.add(attempt_2_for_solution_2);
        attempts_for_solution_2.add(attempt_3_for_solution_2);
        // -----------------------------------------------------------------------------------------
        SolutionHardcoded solution_1 = new SolutionHardcoded();
        solution_1.setLevel(level_1);
        solution_1.setAttempts(attempts_for_solution_1);
        userManager.addSolution(solution_1);

        SolutionHardcoded solution_2 = new SolutionHardcoded();
        solution_2.setLevel(level_2);
        solution_2.setAttempts(attempts_for_solution_2);
        userManager.addSolution(solution_2);

        SolutionHardcoded solution_3 = new SolutionHardcoded();
        solution_3.setLevel(level_3);
        userManager.addSolution(solution_3);
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
    public boolean leaveLobby(String userName, int lobbyID) {
        return lobbyManager.leaveLobby(userName, lobbyID);
    }

    @Override
    public Lobby getLobby(String userName, int lobbyID) {
        return lobbyManager.refreshLobby(userName, lobbyID);
    }

    @Override
    public boolean login(String username) {
        return true;
    }

    @Override
    public boolean logout(String username) {
        return true;
    }

    @Override
    public CompileResult submit(String username, String code, int lobbyId) {
        return new CompileResult() {
            @Override
            public boolean isCompiled() {
                return true;
            }

            @Override
            public String getMessage() {
                return "Compiled!";
            }
        };
    }

    @Override
    public String editSubmittedCode(String username, int lobbyId) {
        return "Cooooooooooooooode!";
    }

    @Override
    public SimulationResult getSimulationResult(String username, int lobbyId) {
        return new SimulationResult() {

            @Override
            public int getId() {
                return 55;
            }

            @Override
            public Date getDate() {
                return new Date();
            }

            @Override
            public boolean isSuccessful(String username) {
                return true;
            }

            @Override
            public String getLog(String username) {
                return "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997\n" +
                        "Nov 07, 2019 2:42:43 AM ru.nsu.fit.markelov.log.LoggingExample main\n" +
                        "INFO: Msg997";
            }
        };
    }
}
