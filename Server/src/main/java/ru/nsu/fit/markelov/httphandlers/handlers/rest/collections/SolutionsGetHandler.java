package ru.nsu.fit.markelov.httphandlers.handlers.rest.collections;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SolutionsGetHandler extends RestHandler {

    private MainManager mainManager;

    public SolutionsGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        String cookieUserName = CookieParser.getCookieUserName(exchange);

        if (cookieUserName == null) {
            throw new ProcessingException("cookieUserName is null.");
        }
        System.out.println("cookieUserName: " + cookieUserName);

        Map<Level, Collection<SimulationResult>> solutions = new HashMap<>();
        for (Level level : mainManager.getLevels()) {
            solutions.put(level, mainManager.getUserSimulationResultsOnLevel(cookieUserName, level.getId()));
        }

        responder.sendResponse(JsonPacker.packSolutions(cookieUserName, solutions));
    }
}
