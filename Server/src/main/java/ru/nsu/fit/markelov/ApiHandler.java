package ru.nsu.fit.markelov;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.*;

public class ApiHandler implements HttpHandler {

    private static final String COOKIE_NAME = "ROBOTICS_USER";

    private SimonsCoreClass simonsCoreClass;
    private Set<String> userNames;

    public ApiHandler(SimonsCoreClass simonsCoreClass) {
        this.simonsCoreClass = simonsCoreClass;
        userNames = new TreeSet<>();
    }

    private String getUserName(HttpExchange exchange) {
        Headers requestHeaders = exchange.getRequestHeaders();
        List<String> cookies = requestHeaders.get("Cookie");

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

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {

            String cookieUserName = "Oleg";//getUserName(exchange);
            /*if (cookieUserName != null) {
                System.out.println("cookieUserName: " + cookieUserName);
            } else {
                System.out.println("cookieUserName: null");
            }*/

            String uri = exchange.getRequestURI().toString();
            /*System.out.println(uri);

            String[] uriParts = uri.split("\\?");
            if (uriParts.length == 2) {
                // OK
                Map<String, List<String>> query_pairs = splitQuery(uriParts[1]);
            } else {
                // bad
            }*/

            System.out.println(uri);
            String jsonStr;
            /*if (uri.startsWith("/api/method/sign.login")) {
                String[] parts = uri.split("\\?");
                if (parts.length == 2) {
                    Map<String, List<String>> params = splitQuery(parts[1]);
                    List<String> idParams = params.get("username");
                    if (idParams.size() == 1) {
                        jsonStr = null;

                        String userName = idParams.get(0);
                        if (userNames.contains(userName)) {
                            exchange.sendResponseHeaders(403, -1);
                            System.out.println(userName + " already exists");
                        } else {
                            List<String> values = new ArrayList<>();
                            values.add(COOKIE_NAME + "=" + userName + ";");
                            exchange.getResponseHeaders().put("Set-Cookie", values);

                            exchange.sendResponseHeaders(200, -1);
                            userNames.add(userName);
                            System.out.println(userName + " added");
                        }
                    } else {
                        jsonStr = null;
                    }
                } else {
                    jsonStr = null;
                }
            } else if (uri.startsWith("/api/method/sign.logout")) {
                jsonStr = null;

                List<String> values = new ArrayList<>();
                values.add("ROBOTICS_USER=NULL;");
                exchange.getResponseHeaders().put("Set-Cookie", values);

                exchange.sendResponseHeaders(200, -1);
                if (cookieUserName != null) {
                    System.out.println(cookieUserName + " removed");
                    userNames.remove(cookieUserName);
                }
            } else */if (uri.startsWith("/api/method/lobbies.get")) {
                jsonStr = simonsCoreClass.getListOfLobbies();
            } else if (uri.startsWith("/api/method/levels.get")) {
                jsonStr = simonsCoreClass.getLevels();
            } else if (uri.startsWith("/api/method/solutions.get")) {
                if (cookieUserName != null) {
                    jsonStr = simonsCoreClass.getSolutions(cookieUserName);
                } else {
                    jsonStr = null;
                }
            } else if (uri.startsWith("/api/method/lobby.join")) {
                String[] parts = uri.split("\\?");
                if (parts.length == 2) {
                    Map<String, List<String>> params = splitQuery(parts[1]);
                    List<String> idParams = params.get("id");
                    if (idParams.size() == 1) {
                        try {
                            if (cookieUserName != null) {
                                jsonStr = simonsCoreClass.joinLobby(cookieUserName, Integer.parseInt(idParams.get(0)));
                            } else {
                                jsonStr = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                            jsonStr = null;
                        }
                    } else {
                        jsonStr = null;
                    }
                } else {
                    jsonStr = null;
                }
            } else if (uri.startsWith("/api/method/lobby.create")) {
                String[] parts = uri.split("\\?");
                if (parts.length == 2) {
                    Map<String, List<String>> params = splitQuery(parts[1]);
                    List<String> idParams = params.get("id");
                    if (idParams.size() == 1) {
                        try {
                            if (cookieUserName != null) {
                                jsonStr = simonsCoreClass.createLobby(cookieUserName, Integer.parseInt(idParams.get(0)));
                            } else {
                                jsonStr = null;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                            jsonStr = null;
                        }
                    } else {
                        jsonStr = null;
                    }
                } else {
                    jsonStr = null;
                }
            } else {
                jsonStr = null;
            }

            if (jsonStr != null) {
                byte[] bytes = jsonStr.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } else {
                exchange.sendResponseHeaders(204, -1);
            }

            exchange.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private Map<String, List<String>> splitQuery(String uri) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = uri.split("&");

        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }

        return query_pairs;
    }
}
