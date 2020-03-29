package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.DebugUtil;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LogInHandler implements HttpHandler {

    private MainManager mainManager;

    public LogInHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String cookieUserName = CookieParser.getCookieUserName(exchange);
            DebugUtil.printCookieUserName(cookieUserName);

            UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
            String userName = uriParametersParser.getStringParameter("username");

            if (userName != null) {
                boolean loggedIn = mainManager.login(userName);

                if (loggedIn) {
                    List<String> values = new ArrayList<>();
                    values.add(CookieParser.COOKIE_NAME + "=" + userName + ";");
                    exchange.getResponseHeaders().put("Set-Cookie", values);
                }

                byte[] bytes = JsonPacker.packLoggingIn(loggedIn).getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } else {
                exchange.sendResponseHeaders(204, -1);
                System.out.println("userName is null");
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
