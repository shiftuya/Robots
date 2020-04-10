package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
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
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String userName = uriParametersParser.getStringParameter("username");

        if (userName == null) {
            throw new ProcessingException("Username is null.");
        }

        mainManager.removeUser(cookieHandler.getCookie(), userName);

        responder.sendResponse();
    }
}
