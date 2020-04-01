package ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LobbyLeaveHandler extends RestHandler {

    private MainManager mainManager;

    public LobbyLeaveHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        String cookieUserName = CookieParser.getCookieUserName(exchange);

        if (cookieUserName == null) {
            throw new ProcessingException("cookieUserName is null.");
        }
        System.out.println("cookieUserName: " + cookieUserName);

        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        if (id == null) {
            throw new ProcessingException("Id is null.");
        }

        responder.sendResponse(JsonPacker.packLeavingLobby(mainManager.leaveLobby(cookieUserName, id)));
    }
}
