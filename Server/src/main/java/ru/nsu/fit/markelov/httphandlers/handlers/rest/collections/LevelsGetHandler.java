package ru.nsu.fit.markelov.httphandlers.handlers.rest.collections;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LevelsGetHandler extends RestHandler {

    private MainManager mainManager;

    public LevelsGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        responder.sendResponse(JsonPacker.packLevels(mainManager.getLevels()));
    }
}
