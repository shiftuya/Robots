package ru.nsu.fit.markelov.httphandlers.handlers.rest.log;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.DebugUtil;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
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
        DebugUtil.printCookieUserName(cookieUserName);                    // TODO delete

        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String userName = uriParametersParser.getStringParameter("username");

        if (userName == null) {
            throw new ProcessingException("userName is null.");
        }

        boolean loggedIn = mainManager.login(userName);

        if (loggedIn) {
            List<String> values = new ArrayList<>();
            values.add(CookieParser.COOKIE_NAME + "=" + userName + ";");
            exchange.getResponseHeaders().put("Set-Cookie", values);
        }

        responder.sendResponse(JsonPacker.packLoggingIn(loggedIn));
    }
}
