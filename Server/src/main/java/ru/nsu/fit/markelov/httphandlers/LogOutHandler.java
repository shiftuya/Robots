package ru.nsu.fit.markelov.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.MainManager;
import ru.nsu.fit.markelov.util.CookieParser;
import ru.nsu.fit.markelov.util.DebugUtil;
import ru.nsu.fit.markelov.util.JsonPacker;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LogOutHandler implements HttpHandler {

    private MainManager mainManager;

    public LogOutHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String cookieUserName = CookieParser.getCookieUserName(exchange);
            DebugUtil.printCookieUserName(cookieUserName);

            if (cookieUserName != null) {
                boolean loggedOut = mainManager.logout(cookieUserName);

                if (loggedOut) {
                    List<String> values = new ArrayList<>();
                    values.add(CookieParser.COOKIE_NAME + "=NULL;");
                    exchange.getResponseHeaders().put("Set-Cookie", values);
                }

                byte[] bytes = JsonPacker.packLoggingOut(loggedOut).getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } else {
                exchange.sendResponseHeaders(204, -1);
                System.out.println("cookieUserName is null");
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
