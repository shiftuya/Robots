package ru.nsu.fit.markelov.managers_hardcoded;

import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.Resource;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;
import ru.nsu.fit.markelov.interfaces.client.playback.Vector3;
import ru.nsu.fit.markelov.objects_hardcoded.LevelHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.LobbyHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.SimulationResultHardcoded;
import ru.nsu.fit.markelov.objects_hardcoded.UserHardcoded;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class MainManagerHardcoded implements MainManager {

    private Set<String> userNames = new TreeSet<>();
    private Set<String> simulators = new TreeSet<>();

    private int token;

    @Override
    public User.UserType getUserType(String token) {
        return null;
    }

    @Override
    public Map<Level, Iterable<SimulationResult>> getSolutions(String token, String userName) {
        return null;
    }

    @Override
    public String login(String userName, String password) {
        return ++token + "";
    }

    @Override
    public void logout(String token) {}

    private LobbyManagerHardcoded lobbyManager;
    private LevelManagerHardcoded levelManager;
    private List<User> users;

    public MainManagerHardcoded() {
        lobbyManager = new LobbyManagerHardcoded();
        levelManager = new LevelManagerHardcoded();
        users = new ArrayList<>();

        // ----- players -----

        UserHardcoded player_1 = new UserHardcoded();
        player_1.setAvatarAddress("/images/person-icon.png");
        player_1.setName("Vasily");
        player_1.setType(User.UserType.Admin);
        player_1.setBlocked(false);
        player_1.setLastActive(new Date());
        users.add(player_1);

        UserHardcoded player_2 = new UserHardcoded();
        player_2.setAvatarAddress("/images/person-icon.png");
        player_2.setName("Simon");
        player_2.setType(User.UserType.Teacher);
        player_2.setBlocked(false);
        player_2.setLastActive(new Date());
        users.add(player_2);

        UserHardcoded player_3 = new UserHardcoded();
        player_3.setAvatarAddress("/images/person-icon.png");
        player_3.setName("Ivan");
        player_3.setType(User.UserType.Student);
        player_3.setBlocked(true);
        player_3.setLastActive(new Date());
        users.add(player_3);

        UserHardcoded player_4 = new UserHardcoded();
        player_4.setAvatarAddress("/images/person-icon.png");
        player_4.setName("Oleg");
        player_4.setType(User.UserType.Student);
        player_4.setBlocked(false);
        player_4.setLastActive(new Date());
        users.add(player_4);

        // ----- levels -----

        LevelHardcoded level_1 = new LevelHardcoded();
        level_1.setId(1);
        level_1.setIconAddress("/images/labyrinth-icon.png");
        level_1.setName("Labyrinth");
        level_1.setDifficulty(Level.LevelDifficulty.Easy);
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
        level_2.setDifficulty(Level.LevelDifficulty.Medium);
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
        level_3.setDifficulty(Level.LevelDifficulty.Hard);
        level_3.setType("Multiplayer (2)");
        level_3.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...");
        level_3.setRules("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        level_3.setGoal("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");
        level_3.setMinPlayers(2);
        level_3.setMaxPlayers(2);
        level_3.setActive(false);
        levelManager.addLevel(level_3);

        // ----- lobbies -----

        List<User> players_for_lobby_1 = new ArrayList<>();
        players_for_lobby_1.add(player_2);
        LobbyHardcoded lobby_1 = new LobbyHardcoded();
        lobby_1.setId(1);
        /*lobby_1.setHostAvatarAddress("/images/person-icon.png");
        lobby_1.setHostName("Simon");*/
        lobby_1.setLevel(level_3);
        lobby_1.setCurrentPlayersAmount(1);
        lobby_1.setAcceptablePlayersAmount(2);
        lobby_1.setUsers(players_for_lobby_1);
        lobbyManager.addLobby(lobby_1);

        List<User> players_for_lobby_2 = new ArrayList<>();
        players_for_lobby_2.add(player_1);
        players_for_lobby_2.add(player_2);
        players_for_lobby_2.add(player_4);
        LobbyHardcoded lobby_2 = new LobbyHardcoded();
        lobby_2.setId(2);
        /*lobby_2.setHostAvatarAddress("/images/person-icon.png");
        lobby_2.setHostName("Vasily");*/
        lobby_2.setLevel(level_2);
        lobby_2.setCurrentPlayersAmount(3);
        lobby_2.setAcceptablePlayersAmount(4);
        lobby_2.setUsers(players_for_lobby_2);
        lobbyManager.addLobby(lobby_2);

        List<User> players_for_lobby_3 = new ArrayList<>();
        players_for_lobby_3.add(player_4);
        LobbyHardcoded lobby_3 = new LobbyHardcoded();
        lobby_3.setId(3);
        /*lobby_3.setHostAvatarAddress("/images/person-icon.png");
        lobby_3.setHostName("Ivan");*/
        lobby_3.setLevel(level_3);
        lobby_3.setCurrentPlayersAmount(1);
        lobby_3.setAcceptablePlayersAmount(2);
        lobby_3.setUsers(players_for_lobby_3);
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

        simulators.add("robotics.nsu.ru/simulators/1");
        simulators.add("robotics.nsu.ru/simulators/2");
    }

    @Override
    public List<Lobby> getLobbies(String token) {
        return lobbyManager.getLobbies();
    }

    @Override
    public List<Level> getLevels(String token) {
        if (false) {
            throw new ProcessingException("qwerty");
        }

        return levelManager.getLevels();
//        return new ArrayList<>();
    }

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
    public void leaveLobby(String userName, int lobbyID) {
        lobbyManager.leaveLobby(userName, lobbyID);
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
    public SimulationResult getSimulationResult(String username, int lobbyId) {
        return new SimulationResult() {

            @Override
            public Iterable<User> getUsers() {
                return users;
            }

            @Override
            public int getId() {
                return 55;
            }

            @Override
            public Date getDate() {
                return new Date();
            }

            @Override
            public Playback getPlayback() {
                return new Playback() {
                    @Override
                    public int getFramesCount() {
                        return 600;
                    }

                    @Override
                    public Map<String, Integer> getRobots() {
                        Map<String, Integer> robots = new TreeMap<>();
                        robots.put("Oleg", 0);
                        robots.put("Simon", 1);
                        robots.put("Vasily", 3);

                        return robots;
                    }

                    @Override
                    public List<List<GameObjectState>> getGameObjectStates() {
                        List<GameObjectState> gameObjectStates_1 = new ArrayList<>();
                        float x = 0;
                        for (int i = 0; i < 600; i++, x += 0.5f) {
                            final int frame = i;
                            final float posX = x;
                            gameObjectStates_1.add(new GameObjectState() {
                                @Override
                                public int getStartingFrame() {
                                    return frame;
                                }

                                @Override
                                public int getEndingFrame() {
                                    return frame + 1;
                                }

                                @Override
                                public Vector3 getPosition() {
                                    return new Vector3() {
                                        @Override
                                        public float getX() {
                                            return posX;
                                        }

                                        @Override
                                        public float getY() {
                                            return 25;
                                        }
                                    };
                                }

                                @Override
                                public Vector3 getDimension() {
                                    return new Vector3() {
                                        @Override
                                        public float getX() {
                                            return 100;
                                        }

                                        @Override
                                        public float getY() {
                                            return 50;
                                        }

                                        @Override
                                        public float getZ() {
                                            return 75;
                                        }
                                    };
                                }

                                @Override
                                public Vector3 getRotation() {
                                    return new Vector3() {
                                        @Override
                                        public float getY() {
                                            return frame;
                                        }
                                    };
                                }

                                @Override
                                public int getColor() {
                                    return 0x00ff00;
                                }

                                @Override
                                public Map<String, String> getSensorValues() {
                                    Map<String, String> sensors = new TreeMap<>();
                                    sensors.put("Sensor 1", posX+"");
                                    sensors.put("Sensor 2", frame+"");

                                    return sensors;
                                }
                            });
                        }

                        List<GameObjectState> gameObjectStates_2 = new ArrayList<>();
                        float z = 0;
                        for (int i = 0; i < 600; i += 10, z += 5f) {
                            final int frame = i;
                            final float posZ = z;
                            gameObjectStates_2.add(new GameObjectState() {
                                @Override
                                public int getStartingFrame() {
                                    return frame;
                                }

                                @Override
                                public int getEndingFrame() {
                                    return frame + 10;
                                }

                                @Override
                                public Vector3 getPosition() {
                                    return new Vector3() {
                                        @Override
                                        public float getY() {
                                            return 25;
                                        }

                                        @Override
                                        public float getZ() {
                                            return posZ;
                                        }
                                    };
                                }

                                @Override
                                public int getColor() {
                                    return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
                                }

                                @Override
                                public Map<String, String> getSensorValues() {
                                    Map<String, String> sensors = new TreeMap<>();
                                    sensors.put("Sensor 1", frame+"");

                                    return sensors;
                                }
                            });
                        }

                        List<GameObjectState> gameObjectStates_3 = new ArrayList<>();
                        gameObjectStates_3.add(new GameObjectState() {
                            @Override
                            public int getStartingFrame() {
                                return 0;
                            }

                            @Override
                            public int getEndingFrame() {
                                return 600;
                            }

                            @Override
                            public Vector3 getPosition() {
                                return new Vector3() {
                                    @Override
                                    public float getY() {
                                        return 250;
                                    }

                                    @Override
                                    public float getZ() {
                                        return -200;
                                    }
                                };
                            }

                            @Override
                            public Vector3 getDimension() {
                                return new Vector3() {
                                    @Override
                                    public float getX() {
                                        return 500;
                                    }

                                    @Override
                                    public float getY() {
                                        return 500;
                                    }

                                    @Override
                                    public float getZ() {
                                        return 10;
                                    }
                                };
                            }

                            @Override
                            public int getColor() {
                                return 0xff0000;
                            }
                        });

                        List<GameObjectState> gameObjectStates_4 = new ArrayList<>();
                        int y = 0;
                        for (int i = 100; i < 200; i++, y++) {
                            final int frame = i;
                            final int posY = y;
                            gameObjectStates_4.add(new GameObjectState() {
                                @Override
                                public int getStartingFrame() {
                                    return frame;
                                }

                                @Override
                                public int getEndingFrame() {
                                    return frame + 1;
                                }

                                @Override
                                public Vector3 getPosition() {
                                    return new Vector3() {
                                        @Override
                                        public float getY() {
                                            return posY;
                                        }
                                    };
                                }
                            });
                        }
                        int finalY = y;
                        gameObjectStates_4.add(new GameObjectState() {
                            @Override
                            public int getStartingFrame() {
                                return 300;
                            }

                            @Override
                            public int getEndingFrame() {
                                return 400;
                            }

                            @Override
                            public Vector3 getPosition() {
                                return new Vector3() {
                                    @Override
                                    public float getY() {
                                        return finalY - 1;
                                    }
                                };
                            }
                        });
                        for (int i = 400; i < 500; i += 10, y += 10) {
                            final int frame = i;
                            final int posY = y;
                            gameObjectStates_4.add(new GameObjectState() {
                                @Override
                                public int getStartingFrame() {
                                    return frame;
                                }

                                @Override
                                public int getEndingFrame() {
                                    return frame + 10;
                                }

                                @Override
                                public Vector3 getPosition() {
                                    return new Vector3() {
                                        @Override
                                        public float getY() {
                                            return posY;
                                        }
                                    };
                                }
                            });
                        }

                        List<List<GameObjectState>> gameObjectsStates = new ArrayList<>();
                        gameObjectsStates.add(gameObjectStates_1);
                        gameObjectsStates.add(gameObjectStates_2);
                        gameObjectsStates.add(gameObjectStates_3);
                        gameObjectsStates.add(gameObjectStates_4);

                        return gameObjectsStates;
                    }
                };
            }

            @Override
            public boolean isSuccessful(String username) {
                return true;
            }

        };
    }

    @Override
    public String getLog(String token, String username, int simulationResultId) {
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
    public String getScript(String token, String username, int simulationResultId) {
        return "class Clazz { }";
    }

    @Override
    public void submitLevel(String token, boolean create, Integer levelID, String name,
                            String difficulty, Integer minPlayers, Integer maxPlayers,
                            Resource iconResource, String description, String rules, String goal,
                            Collection<Resource> levelResources, String code, String language) {
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
            return;
        } else {
            System.out.println("NOT null");
        }

        try {
            Files.write(Paths.get("src/main/resources/images/icons/" + iconResource.getName()), iconResource.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Level getLevel(String token, int levelID) {
        return levelManager.getLevels().get(levelID - 1);
    }

    @Override
    public void deleteLevel(String token, int levelID) {
        levelManager.getLevels().remove(levelID - 1);
    }

    @Override
    public List<String> getSimulators(String token) {
        return new ArrayList<>(simulators);
    }

    @Override
    public void addSimulator(String token, String url) {
        simulators.add(url);
    }

    @Override
    public void removeSimulator(String token, String url) {
        simulators.remove(url);
    }

    @Override
    public Collection<User> getUsers(String userName) {
        return users;
    }

    @Override
    public User getUser(String token, String userName) {
        return users.get(0);
    }

    @Override
    public String getUserName(String token) {
        return "null";
    }

    @Override
    public boolean isSimulationFinished(String token, int lobbyId) {
        return false;
    }

    @Override
    public void submitUser(String token, boolean create, String userName, String password, String type, Resource avatarResource) {

    }

    @Override
    public void blockUser(String token, String userName, boolean block) {

    }

    @Override
    public void removeUser(String token, String userName) {

    }

    @Override
    public Playback getPlayback(String token, int simulationResultId) {
        return null;
    }
}
