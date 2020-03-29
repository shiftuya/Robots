package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
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
            Responder responder = new Responder(exchange, oStream);

            String error = levelInput.getError();
            if (!error.isEmpty()) {
                responder.sendError(error);
                return;
            }

            if (
                mainManager.createLevel(levelInput.getName(), levelInput.getDifficulty(),
                    levelInput.getPlayers(), levelInput.getIconResource(), levelInput.getDescription(),
                    levelInput.getRules(), levelInput.getGoal(), levelInput.getLevelResources(),
                    levelInput.getCode())
            ) {
                responder.sendResponse("true");
            } else {
                responder.sendResponse("false");
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
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
