package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.inputs.UserInput;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;

public class UserSubmitHandler extends RestHandler {

    private MainManager mainManager;
    private boolean create;

    public UserSubmitHandler(MainManager mainManager, boolean create) {
        this.mainManager = mainManager;
        this.create = create;
    }

    @Override
    protected void respond(HttpExchange exchange, CookieHandler cookieHandler, Responder responder) throws IOException {
        UserInput userInput = new UserInput();

        FormDataParser formDataParser = new FormDataParser(exchange) {
            @Override
            protected void addFiles(FormDataHandler.MultiPart part) {
                if (part.name.equals("avatar")) {
                    userInput.getAvatarResource().setName(part.filename).setBytes(part.bytes);
                }
            }
        };

        userInput.setName(formDataParser.getFieldValue("name"));
        userInput.setPassword(formDataParser.getFieldValue("password"));
        userInput.setRepeatPassword(formDataParser.getFieldValue("repeat_password"));
        userInput.setType(formDataParser.getFieldValue("type"));

        userInput.prepare();

        String error = userInput.getError();
        if (!error.isEmpty()) {
            throw new ProcessingException(error);
        }

        mainManager.submitUser(
            cookieHandler.getCookie(),
            create,
            userInput.getName(),
            userInput.getPassword(),
            userInput.getType(),
            userInput.getAvatarResource()
        );

        responder.sendResponse();
    }
}
