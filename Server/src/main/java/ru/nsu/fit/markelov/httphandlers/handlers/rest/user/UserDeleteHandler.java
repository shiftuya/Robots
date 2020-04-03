package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class UserDeleteHandler extends RestHandler {

    private MainManager mainManager;

    public UserDeleteHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String username = uriParametersParser.getStringParameter("username");

        if (username == null) {
            throw new ProcessingException("Username is null.");
        }

        mainManager.removeUser(username);

        responder.sendResponse("OK");
    }
}
