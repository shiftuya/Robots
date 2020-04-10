package ru.nsu.fit.markelov.httphandlers.handlers.rest.simulator;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class SimulatorAddHandler extends RestHandler {

    private MainManager mainManager;

    public SimulatorAddHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String url = uriParametersParser.getStringParameter("url");

        if (url == null) {
            throw new ProcessingException("Url is null.");
        }

        mainManager.addSimulator(cookieHandler.getCookie(), url);

        responder.sendResponse();
    }
}
