package ru.nsu.fit.markelov.httphandlers.handlers.rest.log;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LogOutHandler extends RestHandler {

    private MainManager mainManager;

    public LogOutHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        mainManager.logout(cookieHandler.getCookie());

        cookieHandler.putCookie("NULL");

        responder.sendResponse();
    }
}
