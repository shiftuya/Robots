package ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.DebugUtil;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;

import java.io.IOException;
import java.io.OutputStream;

public class LobbyJoinHandler implements HttpHandler {

    private MainManager mainManager;

    public LobbyJoinHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String cookieUserName = CookieParser.getCookieUserName(exchange);
        DebugUtil.printCookieUserName(cookieUserName);

        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        try (OutputStream oStream = exchange.getResponseBody()) {
            String jsonStr;
            if (cookieUserName != null && id != null) {
                Lobby lobby = mainManager.joinLobby(cookieUserName, id);

                if (lobby == null) {
                    jsonStr = new JSONObject().put("response", JSONObject.NULL).toString();
                } else {
                    jsonStr = JsonPacker.packLobby(lobby);
                }

                byte[] bytes = jsonStr.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } else {
                exchange.sendResponseHeaders(204, -1);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
