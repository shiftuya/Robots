package ru.nsu.fit.markelov.httphandlers.handlers.rest.log;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataParser;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LogInHandler extends RestHandler {

    private final MainManager mainManager;

    public LogInHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        FormDataParser formDataParser = new FormDataParser(exchange) {
            @Override
            protected void addFiles(FormDataHandler.MultiPart part) {}
        };

        String userName = formDataParser.getFieldValue("name");
        String password = formDataParser.getFieldValue("password");

        String token = mainManager.login(userName, password);

        //cookieHandler.printCookieDEBUG();
        cookieHandler.putCookie(token);

        String userType = mainManager.getUserType(token).toString();

        responder.sendResponse(JsonPacker.packLogIn(token, userName, userType));
    }
}
