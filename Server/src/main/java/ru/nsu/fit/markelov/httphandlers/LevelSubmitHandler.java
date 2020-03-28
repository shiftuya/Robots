package ru.nsu.fit.markelov.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.Resource;
import ru.nsu.fit.markelov.util.FormDataHandler;
import ru.nsu.fit.markelov.util.LevelResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LevelSubmitHandler implements HttpHandler {

    private MainManager mainManager;

    public LevelSubmitHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("LevelSubmitHandler");

        Map<String, FormDataHandler.MultiPart> textParametersMap = new TreeMap<>();
        LevelResource iconResource = new LevelResource();
        List<Resource> levelResources = new ArrayList<>();

        try {
            new FormDataHandler() {
                @Override
                public void handle(HttpExchange httpExchange, List<MultiPart> parts) {
                    System.out.println(parts.size());
                    for (MultiPart part : parts) {
                        if (part.type == PartType.FILE) {
                            if (part.name.equals("icon")) {
                                iconResource.setName(part.filename).setBytes(part.bytes);
                            } else {
                                levelResources.add(new LevelResource().setName(part.filename).setBytes(part.bytes));
                            }
                        } else {
                            textParametersMap.put(part.name, part);
                        }
                    }
                    System.out.println(parts.size());
                }
            }.handle(exchange);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try (InputStream ios = exchange.getRequestBody()) {
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String name = textParametersMap.get("name").value;
        String difficulty = textParametersMap.get("difficulty").value;
        String players = textParametersMap.get("players").value;
        String description = textParametersMap.get("description").value;
        String rules = textParametersMap.get("rules").value;
        String goal = textParametersMap.get("goal").value;
        String code = textParametersMap.get("code").value;

        try (OutputStream oStream = exchange.getResponseBody()) {
            if (!name.isEmpty() && !difficulty.isEmpty() && !players.isEmpty() && !description.isEmpty() && !rules.isEmpty() && !goal.isEmpty() && !code.isEmpty()) {
                System.out.println("valid");
                if (mainManager.createLevel(name, difficulty, Integer.parseInt(players), iconResource, description, rules, goal, levelResources, code)) {
                    System.out.println("true");
                    exchange.sendResponseHeaders(200, -1);
                } else {
                    System.out.println("false");
                    exchange.sendResponseHeaders(204, -1);
                }
            } else {
                System.out.println("invalid");
                exchange.sendResponseHeaders(204, -1);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
