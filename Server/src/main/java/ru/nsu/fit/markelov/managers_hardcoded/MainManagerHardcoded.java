package ru.nsu.fit.markelov.managers_hardcoded;

import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.Playback;
import ru.nsu.fit.markelov.interfaces.client.Player;
import ru.nsu.fit.markelov.interfaces.client.Resource;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.objects_hardcoded.LevelHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.LobbyHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.PlayerHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.SimulationResultHardcoded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainManagerHardcoded implements MainManager {

    private Set<String> userNames = new TreeSet<>();

    @Override
    public boolean login(String userName) {
        if (userNames.contains(userName)) {
            System.out.println(userName + " has already logged in!");

            return false;
        } else {
            userNames.add(userName);
            System.out.println(userName + " logged in.");

            return true;
        }
    }

    @Override
    public boolean logout(String userName) {
        if (userNames.contains(userName)) {
            userNames.remove(userName);
            System.out.println(userName + " logged out.");

            return true;
        } else {
            System.out.println(userName + " hasn't logged in yet!");

            return false;
        }
    }

    private LobbyManagerHardcoded lobbyManager;
    private LevelManagerHardcoded levelManager;

    public MainManagerHardcoded() {
        lobbyManager = new LobbyManagerHardcoded();
        levelManager = new LevelManagerHardcoded();

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
        level_1.setActive(true);
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
        level_2.setActive(true);
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
        level_3.setActive(false);
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

        try {
            SimulationResultHardcoded attempt_1_for_solution_1 = new SimulationResultHardcoded();
            attempt_1_for_solution_1.setId(1);
            attempt_1_for_solution_1.setDate(new SimpleDateFormat(JsonPacker.DATE_FORMAT).parse("3.9.2019"));
            attempt_1_for_solution_1.setSuccessful(false);

            SimulationResultHardcoded attempt_2_for_solution_1 = new SimulationResultHardcoded();
            attempt_2_for_solution_1.setId(2);
            attempt_2_for_solution_1.setDate(new SimpleDateFormat(JsonPacker.DATE_FORMAT).parse("5.9.2019"));
            attempt_2_for_solution_1.setSuccessful(true);

            List<SimulationResult> attempts_for_solution_1 = new ArrayList<>();
            attempts_for_solution_1.add(attempt_1_for_solution_1);
            attempts_for_solution_1.add(attempt_2_for_solution_1);
            // -----------------------------------------------------------------------------------------
            SimulationResultHardcoded attempt_1_for_solution_2 = new SimulationResultHardcoded();
            attempt_1_for_solution_2.setId(1);
            attempt_1_for_solution_2.setDate(new SimpleDateFormat(JsonPacker.DATE_FORMAT).parse("3.11.2019"));
            attempt_1_for_solution_2.setSuccessful(false);

            SimulationResultHardcoded attempt_2_for_solution_2 = new SimulationResultHardcoded();
            attempt_2_for_solution_2.setId(2);
            attempt_2_for_solution_2.setDate(new SimpleDateFormat(JsonPacker.DATE_FORMAT).parse("5.11.2019"));
            attempt_2_for_solution_2.setSuccessful(false);

            SimulationResultHardcoded attempt_3_for_solution_2 = new SimulationResultHardcoded();
            attempt_3_for_solution_2.setId(3);
            attempt_3_for_solution_2.setDate(new SimpleDateFormat(JsonPacker.DATE_FORMAT).parse("6.11.2019"));
            attempt_3_for_solution_2.setSuccessful(false);

            List<SimulationResult> attempts_for_solution_2 = new ArrayList<>();
            attempts_for_solution_2.add(attempt_1_for_solution_2);
            attempts_for_solution_2.add(attempt_2_for_solution_2);
            attempts_for_solution_2.add(attempt_3_for_solution_2);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Lobby> getLobbies() {
        return lobbyManager.getLobbies();
    }

    @Override
    public List<Level> getLevels() {
        return levelManager.getLevels();
    }

   /* @Override
    public List<Solution> getSolutions(String userName) {
        return null;
    }*/

    @Override
    public Lobby joinLobby(String userName, int lobbyID) {
//        return null;
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
    public Lobby returnToLobby(String userName, int lobbyID) {
        return lobbyManager.refreshLobby(userName, lobbyID);
    }

    @Override
    public CompileResult submit(String username, int lobbyId, String code) {
        return new CompileResult() {
            @Override
            public boolean isSimulated() {
                return true;
            }

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
    public boolean isSimulationFinished(int lobbyId) {
        return true;
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

            @Override
            public Playback getPlayback(String username) {
                return null;
            }
        };
    }

    @Override
    public List<SimulationResult> getUserSimulationResultsOnLevel(String username, int levelId) {
        return new ArrayList<>();
    }

    @Override
    public boolean submitLevel(Integer levelID, String name, String difficulty, Integer minPlayers,
                               Integer maxPlayers, Resource iconResource, String description, String rules,
                               String goal, List<Resource> levelResources, String code, String language) {
        if (levelID == null) {
            System.out.println("null");
        } else {
            System.out.println("NOT null");
        }
        System.out.println(name);
        System.out.println(difficulty);
        System.out.println(minPlayers);
        System.out.println(maxPlayers);
        System.out.println(description);
        System.out.println(rules);
        System.out.println(goal);
        System.out.println(code);

        if (false) {
            throw new ProcessingException("qwerty");
        }

        if (levelResources == null) {
            System.out.println("null");
        } else {
            System.out.println("NOT null");
        }

        if (iconResource == null) {
            System.out.println("null");
            return true;
        } else {
            System.out.println("NOT null");
        }

        try {
            Files.write(Paths.get("src/main/resources/images/icons/" + iconResource.getName()), iconResource.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean deleteLevel(int levelID) {
        return false;
    }

    @Override
    public List<String> getSimulators() {
        return null;
    }

    @Override
    public boolean addSimulator(String url) {
        return false;
    }

    @Override
    public boolean removeSimulator(String url) {
        return false;
    }

    @Override
    public Level getLevel(int levelID) {
        return levelManager.getLevels().get(levelID - 1);
    }
}
