package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.DebugUtil;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionsGetHandler implements HttpHandler {

    private MainManager mainManager;

    public SolutionsGetHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String cookieUserName = CookieParser.getCookieUserName(exchange);
        DebugUtil.printCookieUserName(cookieUserName);

        try (OutputStream oStream = exchange.getResponseBody()) {
            if (cookieUserName != null) {

                Map<Level, List<SimulationResult>> solutions = new HashMap<>();
                for (Level level : mainManager.getLevels(false)) {
                    solutions.put(level, mainManager.getUserSimulationResultsOnLevel(cookieUserName, level.getId()));
                }

                byte[] bytes = JsonPacker.packSolutions(cookieUserName, solutions).getBytes();
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
