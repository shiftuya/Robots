package ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.util.JsonPacker;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.PostRequestBodyParserHARDCODED;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class LobbySubmitHandler extends RestHandler {

    private MainManager mainManager;

    public LobbySubmitHandler(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UriParametersParser uriParametersParser = new UriParametersParser(exchange.getRequestURI().toString());
        Integer id = uriParametersParser.getIntegerParameter("id");

        if (id == null) {
            throw new ProcessingException("Id is null.");
        }

        String code = PostRequestBodyParserHARDCODED.getCodeHARDCODED(exchange.getRequestBody());

        if (code == null) {
            throw new ProcessingException("Code is null.");
        }
        System.out.println(code);

        responder.sendResponse(JsonPacker.packCompileResult(mainManager.submit(cookieHandler.getCookie(), id, code)));

        /*try { // TODO don't catch when Simon does it right
            responder.sendResponse(JsonPacker.packCompileResult(mainManager.submit(cookieHandler.getCookie(), id, code)));
        } catch (RuntimeException e) {
            throw new ProcessingException(e.getClass().getSimpleName());
        }*/
    }
}
