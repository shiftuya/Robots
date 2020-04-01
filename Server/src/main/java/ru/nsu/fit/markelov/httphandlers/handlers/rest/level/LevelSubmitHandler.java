package ru.nsu.fit.markelov.httphandlers.handlers.rest.level;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.inputs.LevelInput;
import ru.nsu.fit.markelov.httphandlers.util.LevelResource;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LevelSubmitHandler extends RestHandler {

    private MainManager mainManager;

    public LevelSubmitHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
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
                            } else if (part.name.equals("resources")) {
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

        if (textParametersMap.containsKey("id")) {
            levelInput.setId(textParametersMap.get("id").value);
        }
        levelInput.setName(textParametersMap.get("name").value);
        levelInput.setDifficulty(textParametersMap.get("difficulty").value);
        levelInput.setMinPlayers(textParametersMap.get("min_players").value);
        levelInput.setMaxPlayers(textParametersMap.get("max_players").value);
        levelInput.setDescription(textParametersMap.get("description").value);
        levelInput.setRules(textParametersMap.get("rules").value);
        levelInput.setGoal(textParametersMap.get("goal").value);
        levelInput.setCode(textParametersMap.get("code").value);

        levelInput.prepare();

        String error = levelInput.getError();
        if (!error.isEmpty()) {
            throw new ProcessingException(error);
        }

        try {
            if (mainManager.submitLevel(
                levelInput.getId(),
                levelInput.getName(),
                levelInput.getDifficulty(),
                levelInput.getMinPlayers(),
                levelInput.getMaxPlayers(),
                levelInput.getIconResource(),
                levelInput.getDescription(),
                levelInput.getRules(),
                levelInput.getGoal(),
                levelInput.getLevelResources(),
                levelInput.getCode(),
                "groovy"
            )) {
                responder.sendResponse(levelInput.getId() == null ? "New level is created." : "The level is updated.");
            } else {
                responder.sendResponse(levelInput.getId() == null ? "New level is not created." : "The level is not updated.");
            }
        } catch (NumberFormatException e) {
            throw new ProcessingException("level_id is not a number");
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
