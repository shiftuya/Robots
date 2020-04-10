package ru.nsu.fit.markelov.httphandlers.util.parsers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.List;

public class CookieParser {

    public static final String COOKIE_NAME = "ROBOTICS_TOKEN";

    public static String getCookieUserName(HttpExchange exchange) {
        Headers requestHeaders = exchange.getRequestHeaders();
        List<String> cookies = requestHeaders.get("Cookie");

        if (cookies == null) {
            return null;
        }

        for (String cookie : cookies) {
            List<HttpCookie> httpCookies = HttpCookie.parse(cookie);
            for (HttpCookie httpCookie : httpCookies) {
                if (httpCookie.getName().endsWith(COOKIE_NAME)) {
                    if (httpCookie.getValue().equals("NULL")) {
                        return null;
                    }

                    return httpCookie.getValue();
                }
            }
        }

        return null;
    }

    public static void printCookieDEBUG(String cookie) {
        if (cookie != null) {
            System.out.println("cookie: " + cookie);
        } else {
            System.out.println("cookie: null");
        }
    }
}
