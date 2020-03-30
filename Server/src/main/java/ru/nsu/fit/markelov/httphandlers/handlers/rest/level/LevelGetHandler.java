package ru.nsu.fit.markelov.httphandlers.handlers.rest.level;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.io.OutputStream;

public class LevelGetHandler implements HttpHandler {

    private MainManager mainManager;

    public LevelGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        try (OutputStream oStream = exchange.getResponseBody()) {
            if (id != null) {
                byte[] bytes = JsonPacker.packLevel(mainManager.getLevel(id)).getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);
            } else {
                exchange.sendResponseHeaders(204, -1);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            exchange.close();
        }
    }
}
