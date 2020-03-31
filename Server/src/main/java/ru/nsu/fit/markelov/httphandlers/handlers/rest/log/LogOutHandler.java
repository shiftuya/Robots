package ru.nsu.fit.markelov.httphandlers.handlers.rest.log;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogOutHandler extends RestHandler {

    private MainManager mainManager;

    public LogOutHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        String cookieUserName = CookieParser.getCookieUserName(exchange);

        if (cookieUserName == null) {
            throw new ProcessingException("cookieUserName is null.");
        }
        System.out.println("cookieUserName: " + cookieUserName);

        boolean loggedOut = mainManager.logout(cookieUserName);

        if (loggedOut) {
            List<String> values = new ArrayList<>();
            values.add(CookieParser.COOKIE_NAME + "=NULL;");
            exchange.getResponseHeaders().put("Set-Cookie", values);
        }

        responder.sendResponse(JsonPacker.packLoggingOut(loggedOut));
    }
}
