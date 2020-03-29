package ru.nsu.fit.markelov.httphandlers.util;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class Responder {

    private static final String RESPONSE = "response";
    private static final String ERROR = "error";

    private HttpExchange exchange;
    private OutputStream oStream;

    public Responder(HttpExchange exchange, OutputStream oStream) {
        this.exchange = exchange;
        this.oStream = oStream;
    }

    public void sendResponse(String message) throws IOException {
        send(RESPONSE, message);
    }

    public void sendError(String message) throws IOException {
        send(ERROR, message);
    }

    private void send(String type, String message) throws IOException {
        byte[] bytes = new JSONObject().put(type, message).toString().getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        oStream.write(bytes);
        System.out.println(new String(bytes));
    }
}
