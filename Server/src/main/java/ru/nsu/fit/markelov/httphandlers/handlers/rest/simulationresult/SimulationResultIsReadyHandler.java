package ru.nsu.fit.markelov.httphandlers.handlers.rest.simulationresult;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieParser;
import ru.nsu.fit.markelov.httphandlers.util.DebugUtil;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;

import java.io.IOException;
import java.io.OutputStream;

public class SimulationResultIsReadyHandler implements HttpHandler {

    private MainManager mainManager;

    public SimulationResultIsReadyHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String cookieUserName = CookieParser.getCookieUserName(exchange);
        DebugUtil.printCookieUserName(cookieUserName);

        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        try (OutputStream oStream = exchange.getResponseBody()) {
            if (/*cookieUserName != null && */id != null) {
                byte[] bytes = JsonPacker.packSimulationResultReadiness(mainManager.isSimulationFinished(id)).getBytes();
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
