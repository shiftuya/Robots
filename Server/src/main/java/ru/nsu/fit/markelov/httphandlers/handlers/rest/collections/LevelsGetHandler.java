package ru.nsu.fit.markelov.httphandlers.handlers.rest.collections;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.io.OutputStream;

public class LevelsGetHandler implements HttpHandler {

    private MainManager mainManager;

    public LevelsGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            byte[] bytes = JsonPacker.packLevels(mainManager.getLevels()).getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            oStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
