package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class UserBlockHandler extends RestHandler {

    private MainManager mainManager;

    public UserBlockHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String username = uriParametersParser.getStringParameter("username");
        String block = uriParametersParser.getStringParameter("block");

        if (username == null) {
            throw new ProcessingException("Username is null.");
        }

        if (block == null || (!block.equals("true") && !block.equals("false"))) {
            throw new ProcessingException("Bad input.");
        }

        mainManager.blockUser(username, block.equals("true"));

        responder.sendResponse("OK");
    }
}
