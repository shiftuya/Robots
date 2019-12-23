package ru.nsu.fit.markelov.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.MainManager;
import ru.nsu.fit.markelov.util.CookieParser;
import ru.nsu.fit.markelov.util.DebugUtil;
import ru.nsu.fit.markelov.util.UriParametersParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SignHandlerHARDCODED implements HttpHandler {

    private MainManager mainManager;
    private Set<String> userNames;

    public SignHandlerHARDCODED(MainManager mainManager) {
        this.mainManager = mainManager;
        userNames = new TreeSet<>();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String uri = exchange.getRequestURI().toString();
            System.out.println(uri);

            String cookieUserName = CookieParser.getCookieUserName(exchange);
            DebugUtil.printCookieUserName(cookieUserName);

            if (uri.startsWith("/api/method/sign.login")) {
                UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
                String userName = uriParametersParser.getStringParameter("username");
                if (userNames.contains(userName)) {
                    exchange.sendResponseHeaders(403, -1);
                    System.out.println(userName + " already exists");
                } else {
                    List<String> values = new ArrayList<>();
                    values.add(CookieParser.COOKIE_NAME + "=" + userName + ";");
                    exchange.getResponseHeaders().put("Set-Cookie", values);

                    exchange.sendResponseHeaders(200, -1);
                    userNames.add(userName);
                    System.out.println(userName + " added");
                }
            } else if (uri.startsWith("/api/method/sign.logout")) {
                List<String> values = new ArrayList<>();
                values.add("ROBOTICS_USER=NULL;");
                exchange.getResponseHeaders().put("Set-Cookie", values);

                exchange.sendResponseHeaders(200, -1);
                if (cookieUserName != null) {
                    System.out.println(cookieUserName + " removed");
                    userNames.remove(cookieUserName);
                }
            }

            exchange.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
