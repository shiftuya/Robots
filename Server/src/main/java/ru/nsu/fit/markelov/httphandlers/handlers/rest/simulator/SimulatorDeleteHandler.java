package ru.nsu.fit.markelov.httphandlers.handlers.rest.simulator;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class SimulatorDeleteHandler extends RestHandler {

    private MainManager mainManager;

    public SimulatorDeleteHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String url = uriParametersParser.getStringParameter("url");

        if (url == null) {
            throw new ProcessingException("Url is null.");
        }

        responder.sendResponse(JsonPacker.packSimulatorDelete(mainManager.removeSimulator(url)));
    }
}
