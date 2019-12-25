package ru.nsu.fit.markelov.util;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.List;

public class CookieParser {

    public static final String COOKIE_NAME = "ROBOTICS_USER";

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
}
