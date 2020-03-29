package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.httphandlers.inputs.LevelInput;
import ru.nsu.fit.markelov.httphandlers.util.LevelResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        Map<String, FormDataHandler.MultiPart> textParametersMap = new TreeMap<>();
        LevelInput levelInput = new LevelInput();

        try {
            new FormDataHandler() {
                @Override
                public void handle(HttpExchange httpExchange, List<MultiPart> parts) {
                    for (MultiPart part : parts) {
                        if (part.type == PartType.FILE) {
                            if (part.name.equals("icon")) {
                                levelInput.getIconResource().setName(part.filename).setBytes(part.bytes);
                            } else {
                                levelInput.getLevelResources().add(new LevelResource().setName(part.filename).setBytes(part.bytes));
                            }
                        } else {
                            textParametersMap.put(part.name, part);
                        }
                    }
                }
            }.handle(exchange);
        } catch (IOException e) {
            e.printStackTrace();
        }

        levelInput.setName(textParametersMap.get("name").value);
        levelInput.setDifficulty(textParametersMap.get("difficulty").value);
        levelInput.setPlayers(textParametersMap.get("players").value);
        levelInput.setDescription(textParametersMap.get("description").value);
        levelInput.setRules(textParametersMap.get("rules").value);
        levelInput.setGoal(textParametersMap.get("goal").value);
        levelInput.setCode(textParametersMap.get("code").value);

        try (OutputStream oStream = exchange.getResponseBody()) {
            String error = levelInput.getError();
            if (!error.isEmpty()) {
                sendResponse("error", error, exchange, oStream);
                return;
            }

            if (
                mainManager.createLevel(levelInput.getName(), levelInput.getDifficulty(),
                    levelInput.getPlayers(), levelInput.getIconResource(), levelInput.getDescription(),
                    levelInput.getRules(), levelInput.getGoal(), levelInput.getLevelResources(),
                    levelInput.getCode())
            ) {
                sendResponse("response", "true", exchange, oStream);
            } else {
                sendResponse("response", "false", exchange, oStream);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }

    private void sendResponse(String type, String message, HttpExchange exchange, OutputStream oStream) throws IOException {
        byte[] bytes = new JSONObject().put(type, message).toString().getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        oStream.write(bytes);
        System.out.println(new String(bytes));
    }

    private void printRequestBodyDEBUG(HttpExchange exchange) {
        try (InputStream ios = exchange.getRequestBody()) {
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
