package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ApiHandler implements HttpHandler {

    private static final String RESOURCES_FOLDER = "src/main/resources";

    private SimonsCoreClass simonsCoreClass;

    public ApiHandler(SimonsCoreClass simonsCoreClass) {
        this.simonsCoreClass = simonsCoreClass;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String uri = exchange.getRequestURI().toString();
            /*System.out.println(uri);

            String[] uriParts = uri.split("\\?");
            if (uriParts.length == 2) {
                // OK
                Map<String, List<String>> query_pairs = splitQuery(uriParts[1]);
            } else {
                // bad
            }*/

            String jsonStr;
            if (uri.startsWith("/api/method/lobbies.get")) {
                jsonStr = simonsCoreClass.getListOfLobbies();
            } else if (uri.startsWith("/api/method/levels.get")) {
                jsonStr = simonsCoreClass.getLevels();
            } else if (uri.startsWith("/api/method/solutions.get")) {
                jsonStr = simonsCoreClass.getSolutions();
            } else {
                jsonStr = null;
            }

            if (jsonStr != null) {
                byte[] bytes = jsonStr.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } else {
                exchange.sendResponseHeaders(204, 0);
            }
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
