package ru.nsu.fit.markelov.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.util.JsonPacker;

import java.io.IOException;
import java.io.OutputStream;

public class LobbiesGetHandler implements HttpHandler {

    private MainManager mainManager;

    public LobbiesGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            byte[] bytes = JsonPacker.packLobbies(mainManager.getLobbies()).getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            oStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
