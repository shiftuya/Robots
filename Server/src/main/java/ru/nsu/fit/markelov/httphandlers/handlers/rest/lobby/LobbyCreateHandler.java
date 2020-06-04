package ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LobbyCreateHandler extends RestHandler {

    private MainManager mainManager;

    public LobbyCreateHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");
        Integer playersAmount = uriParametersParser.getIntegerParameter("players_amount");

        if (id == null) {
            throw new ProcessingException("Id is null.");
        }

        if (playersAmount == null) {
            throw new ProcessingException("Players amount is null.");
        }

        responder.sendResponse(mainManager.createLobby(cookieHandler.getCookie(), id, playersAmount).getId() + "");
    }
}
