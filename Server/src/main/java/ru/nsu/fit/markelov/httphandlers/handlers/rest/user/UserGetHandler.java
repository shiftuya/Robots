package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserGetHandler extends RestHandler {

    private MainManager mainManager;

    public UserGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String username = uriParametersParser.getStringParameter("username");

        if (username == null) {
            throw new ProcessingException("Username is null.");
        }

        JSONObject jsonUser = JsonPacker.packUser(mainManager.getUser(username));

        Map<Level, Collection<SimulationResult>> solutions = new HashMap<>();
        for (Level level : mainManager.getLevels()) {
            solutions.put(level, mainManager.getUserSimulationResultsOnLevel(username, level.getId()));
        }

        JSONArray jsonSolutions = JsonPacker.packSolutions(username, solutions);

        responder.sendResponse(JsonPacker.packUserInfo(jsonUser, jsonSolutions));
    }
}
