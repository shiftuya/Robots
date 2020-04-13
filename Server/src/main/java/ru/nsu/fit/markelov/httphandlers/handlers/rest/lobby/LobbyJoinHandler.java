package ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LobbyJoinHandler extends RestHandler {

    private MainManager mainManager;

    public LobbyJoinHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        if (id == null) {
            throw new ProcessingException("Id is null.");
        }

        Lobby lobby = mainManager.joinLobby(cookieHandler.getCookie(), id);
        responder.sendResponse(lobby == null ? ((JSONObject) JSONObject.NULL) : JsonPacker.packLobby(lobby));
    }
}
