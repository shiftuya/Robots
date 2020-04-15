package ru.nsu.fit.markelov.httphandlers.handlers.rest.collections;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SolutionsGetHandler extends RestHandler {

    private MainManager mainManager;

    public SolutionsGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        String userName = mainManager.getUserName(cookieHandler.getCookie());
        
        responder.sendResponse(JsonPacker.packSolutions(userName, mainManager.getSolutions(cookieHandler.getCookie(), userName)));
    }
}
