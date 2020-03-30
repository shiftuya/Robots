package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.io.OutputStream;

public class SimulatorAddHandler implements HttpHandler {

    private MainManager mainManager;

    public SimulatorAddHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        String url = uriParametersParser.getStringParameter("url");

        try (OutputStream oStream = exchange.getResponseBody()) {
            if (url != null) {
                byte[] bytes = JsonPacker.packSimulatorAdd(mainManager.addSimulator(url)).getBytes();
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
