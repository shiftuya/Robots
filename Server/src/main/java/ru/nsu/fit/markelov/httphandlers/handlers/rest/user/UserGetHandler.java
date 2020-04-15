package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserGetHandler extends RestHandler {

    private MainManager mainManager;

    public UserGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String userName = uriParametersParser.getStringParameter("username");

        if (userName == null) {
            throw new ProcessingException("Username is null.");
        }

        JSONObject jsonUser = JsonPacker.packUser(mainManager.getUser(cookieHandler.getCookie(), userName));

        JSONArray jsonSolutions = JsonPacker.packSolutions(userName, mainManager.getSolutions(cookieHandler.getCookie(), userName));

        responder.sendResponse(JsonPacker.packUserInfo(jsonUser, jsonSolutions));
    }
}
