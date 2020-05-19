package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;
import ru.nsu.fit.markelov.interfaces.client.playback.Vector3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class PlaybackServer {

    public interface PlaybackFromSimulator {
        Playback getPlayback();
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(80), 0);

        server.createContext("/", new PlaybackHttpHandler());
        server.createContext("/api/method/playback.get", new PlaybackGetHandler(new PlaybackFromSimulator() {
            @Override
            public Playback getPlayback() {
                return new Playback() {
                    @Override
                    public int getFramesCount() {
                        return 600;
                    }

                    @Override
                    public Map<String, Integer> getRobots() {
                        return null;
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

                                        @Override
                                        public float getZ() {
                                            return 0;
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
                                        public float getX() {
                                            return 0;
                                        }

                                        @Override
                                        public float getY() {
                                            return 0;
                                        }

                                        @Override
                                        public float getZ() {
                                            return 0;
                                        }
                                    };
                                }

                                @Override
                                public int getColor() {
                                    return 0x00ff00;
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
                                        public float getX() {
                                            return 0;
                                        }

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
                                public Vector3 getDimension() {
                                    return new Vector3() {
                                        @Override
                                        public float getX() {
                                            return 50;
                                        }

                                        @Override
                                        public float getY() {
                                            return 50;
                                        }

                                        @Override
                                        public float getZ() {
                                            return 50;
                                        }
                                    };
                                }

                                @Override
                                public Vector3 getRotation() {
                                    return new Vector3() {
                                        @Override
                                        public float getX() {
                                            return 45;
                                        }

                                        @Override
                                        public float getY() {
                                            return 45;
                                        }

                                        @Override
                                        public float getZ() {
                                            return 45;
                                        }
                                    };
                                }

                                @Override
                                public int getColor() {
                                    return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
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
                                    public float getX() {
                                        return 0;
                                    }

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
                            public Vector3 getRotation() {
                                return new Vector3() {
                                    @Override
                                    public float getX() {
                                        return 0;
                                    }

                                    @Override
                                    public float getY() {
                                        return 0;
                                    }

                                    @Override
                                    public float getZ() {
                                        return 0;
                                    }
                                };
                            }

                            @Override
                            public int getColor() {
                                return 0xff0000;
                            }
                        });

                        List<List<GameObjectState>> gameObjectsStates = new ArrayList<>();
                        gameObjectsStates.add(gameObjectStates_1);
                        gameObjectsStates.add(gameObjectStates_2);
                        gameObjectsStates.add(gameObjectStates_3);

                        return gameObjectsStates;
                    }
                };
            }
        }));

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    private static class PlaybackHttpHandler implements HttpHandler {
        private static final String RESOURCES_FOLDER = "src/main/resources/";
        private static final String HTML_PAGE = "playback.html";

        @Override
        public void handle(HttpExchange exchange) {
            try (OutputStream oStream = exchange.getResponseBody()) {
                String uri = exchange.getRequestURI().toString();

                String fileName = RESOURCES_FOLDER + uri;
                Path path = Paths.get(fileName);

                if (Files.isRegularFile(path)) {
                    byte[] bytes = Files.readAllBytes(path);
                    exchange.sendResponseHeaders(200, bytes.length);
                    oStream.write(bytes);

                    return;
                }

                byte[] bytes = Files.readAllBytes(Paths.get(RESOURCES_FOLDER + HTML_PAGE));
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                exchange.close();
            }
        }
    }

    private static class PlaybackGetHandler implements HttpHandler {

        PlaybackFromSimulator playbackFromSimulator;

        public PlaybackGetHandler(PlaybackFromSimulator playbackFromSimulator) {
            this.playbackFromSimulator = playbackFromSimulator;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try (OutputStream oStream = exchange.getResponseBody()) {
                Responder responder = new Responder(exchange, oStream);

                try {
                    responder.sendResponse(JsonPacker.packPlayback(playbackFromSimulator.getPlayback()));
                } catch (ProcessingException e) {
                    responder.sendError(e.getMessage());
                }
            } catch (IOException|RuntimeException e) {
                e.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }
}
