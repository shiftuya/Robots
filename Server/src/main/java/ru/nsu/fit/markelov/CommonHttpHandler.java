package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class CommonHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            byte[] bytes;
            try {
                bytes = Files.readAllBytes(Paths.get("src/main/resources" + exchange.getRequestURI()));
                exchange.sendResponseHeaders(200, bytes.length);
            } catch (NoSuchFileException e) {
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());

                bytes = Files.readAllBytes(Paths.get("src/main/resources/404.html"));
                exchange.sendResponseHeaders(404, bytes.length);
            }
            oStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
