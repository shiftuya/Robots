package ru.nsu.fit.markelov.httphandlers.handlers.rest.level;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.inputs.LevelInput;
import ru.nsu.fit.markelov.httphandlers.util.FileResource;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.io.InputStream;

public class LevelSubmitHandler extends RestHandler {

    private MainManager mainManager;

    public LevelSubmitHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        LevelInput levelInput = new LevelInput();

        FormDataParser formDataParser = new FormDataParser(exchange) {
            @Override
            protected void addFiles(FormDataHandler.MultiPart part) {
                if (part.name.equals("icon")) {
                    levelInput.getIconResource().setName(part.filename).setBytes(part.bytes);
                } else if (part.name.equals("resources")) {
                    levelInput.getLevelResources().add(new FileResource().setName(part.filename).setBytes(part.bytes));
                }
            }
        };

        levelInput.setId(formDataParser.getFieldValue("id"));
        levelInput.setName(formDataParser.getFieldValue("name"));
        levelInput.setDifficulty(formDataParser.getFieldValue("difficulty"));
        levelInput.setMinPlayers(formDataParser.getFieldValue("min_players"));
        levelInput.setMaxPlayers(formDataParser.getFieldValue("max_players"));
        levelInput.setDescription(formDataParser.getFieldValue("description"));
        levelInput.setRules(formDataParser.getFieldValue("rules"));
        levelInput.setGoal(formDataParser.getFieldValue("goal"));
        levelInput.setCode(formDataParser.getFieldValue("code"));

        levelInput.prepare();

        String error = levelInput.getError();
        if (!error.isEmpty()) {
            throw new ProcessingException(error);
        }

        try {
            mainManager.submitLevel(
                cookieHandler.getCookie(),
                levelInput.getId() == null,
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
            );
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
