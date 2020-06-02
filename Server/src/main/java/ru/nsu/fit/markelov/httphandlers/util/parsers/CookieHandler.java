package ru.nsu.fit.markelov.httphandlers.util.parsers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class CookieHandler {

    public static final String COOKIE_NAME = "ROBOTICS_USER";

    private final HttpExchange exchange;
    private String cookie;

    public CookieHandler(HttpExchange exchange) {
        this.exchange = exchange;
        Headers requestHeaders = exchange.getRequestHeaders();
        List<String> cookies = requestHeaders.get("Cookie");

        if (cookies == null) {
            return;
        }

        for (String cookie : cookies) {
            List<HttpCookie> httpCookies = new ArrayList<>();
            for (String singleCookie : cookie.split(";")) {
                httpCookies.addAll(HttpCookie.parse(singleCookie));
            }

            for (HttpCookie httpCookie : httpCookies) {
                if (httpCookie.getName().endsWith(COOKIE_NAME)) {
                    if (httpCookie.getValue().equals("NULL")) {
                        return;
                    }

                    this.cookie = httpCookie.getValue();
                    return;
                }
            }
        }
    }

    public String getCookie() {
        return cookie;
    }

    public void putCookie(String token) {
        List<String> values = new ArrayList<>();
        values.add(COOKIE_NAME + "=" + token);
        exchange.getResponseHeaders().put("Set-Cookie", values);
    }

    public void printCookieDEBUG() {
        if (cookie != null) {
            System.out.println("cookie: " + cookie);
        } else {
            System.out.println("cookie: null");
        }
    }
}
