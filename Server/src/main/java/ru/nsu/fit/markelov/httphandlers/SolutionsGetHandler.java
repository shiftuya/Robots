package ru.nsu.fit.markelov.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.client.Solution;
import ru.nsu.fit.markelov.util.CookieParser;
import ru.nsu.fit.markelov.util.DebugUtil;
import ru.nsu.fit.markelov.util.JsonPacker;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
                List<Solution> solutions = new ArrayList<>();
                for (Level level : mainManager.getLevels(false)) {
                    solutions.add(new Solution() {
                        @Override
                        public Level getLevel() {
                            return level;
                        }

                        @Override
                        public List<SimulationResult> getSimulationResults() {
//                            System.out.println(mainManager.getUserSimulationResultsOnLevel(cookieUserName, level.getId()));
                            return mainManager.getUserSimulationResultsOnLevel(cookieUserName, level.getId());
                        }
                    });
                }
                byte[] bytes = JsonPacker.packSolutions(cookieUserName, solutions).getBytes();
//                byte[] bytes = packSolutions(cookieUserName, mainManager.getSolutions(cookieUserName)).getBytes();
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
