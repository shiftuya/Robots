package ru.nsu.fit.markelov.http_handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonHttpHandler implements HttpHandler {

    private static final String RESOURCES_FOLDER = "src/main/resources";

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String uri = exchange.getRequestURI().toString();
            if (uri.equals("/")) {
                uri = "/index.html";
            }//System.out.println(uri);

            String fileName = RESOURCES_FOLDER + uri;
            Path path = Paths.get(fileName);
            int responseCode = 200;

            if (!Files.isRegularFile(path)) {
                path = Paths.get(RESOURCES_FOLDER + "/404.html");
                responseCode = 404;
            }

            byte[] bytes = Files.readAllBytes(path);
            exchange.sendResponseHeaders(responseCode, bytes.length);

            oStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
