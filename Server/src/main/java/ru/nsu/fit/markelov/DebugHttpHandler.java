package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DebugHttpHandler implements HttpHandler {

    private static final String RESOURCES_FOLDER = "src/main/resources";

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String uri = exchange.getRequestURI().toString();
            System.out.println(uri);

            byte[] bytes = "OK".getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            oStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
