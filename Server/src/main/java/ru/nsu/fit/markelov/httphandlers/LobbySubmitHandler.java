package ru.nsu.fit.markelov.httphandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.interfaces.MainManager;
import ru.nsu.fit.markelov.util.CookieParser;
import ru.nsu.fit.markelov.util.DebugUtil;
import ru.nsu.fit.markelov.util.JsonPacker;
import ru.nsu.fit.markelov.util.PostRequestBodyParserHARDCODED;
import ru.nsu.fit.markelov.util.UriParametersParser;

import java.io.IOException;
import java.io.OutputStream;

public class LobbySubmitHandler implements HttpHandler {

    private MainManager mainManager;

    public LobbySubmitHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String cookieUserName = CookieParser.getCookieUserName(exchange);
        DebugUtil.printCookieUserName(cookieUserName);

        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        String code = PostRequestBodyParserHARDCODED.getCodeHARDCODED(exchange.getRequestBody());

        try (OutputStream oStream = exchange.getResponseBody()) {
            if (cookieUserName != null && code != null && id != null) {
                byte[] bytes = JsonPacker.packCompileResult(mainManager.submit(cookieUserName, code, id)).getBytes();
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