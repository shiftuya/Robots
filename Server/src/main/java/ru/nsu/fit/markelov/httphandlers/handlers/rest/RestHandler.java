package ru.nsu.fit.markelov.httphandlers.handlers.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.interfaces.ProcessingException;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            CookieHandler cookieHandler = new CookieHandler(exchange);
            cookieHandler.printCookieDEBUG();

            Responder responder = new Responder(exchange, oStream);

            try {
                respond(exchange, cookieHandler, responder);
            } catch (ProcessingException e) {
                responder.sendError(e.getMessage());
            }
        } catch (IOException|RuntimeException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    protected abstract void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException;
}
