package ru.nsu.fit.markelov.httphandlers.handlers.rest.log;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataParser;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogInHandler extends RestHandler {

    private MainManager mainManager;

    public LogInHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        String cookieUserName = CookieParser.getCookieUserName(exchange); // TODO delete
        CookieParser.printCookieDEBUG(cookieUserName);                    // TODO delete

        FormDataParser formDataParser = new FormDataParser(exchange) {
            @Override
            protected void addFiles(FormDataHandler.MultiPart part) {}
        };

        String userName = formDataParser.getFieldValue("name");
        String password = formDataParser.getFieldValue("password");

        String token = mainManager.login(userName, password);

        List<String> values = new ArrayList<>();
        values.add(CookieParser.COOKIE_NAME + "=" + token + ";");
        exchange.getResponseHeaders().put("Set-Cookie", values);

        System.out.println("new token: " + token);

        responder.sendResponse();
    }
}
