package ru.nsu.fit.markelov.httphandlers.handlers.rest.user;

import com.sun.net.httpserver.HttpExchange;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.RestHandler;
import ru.nsu.fit.markelov.httphandlers.inputs.UserInput;
import ru.nsu.fit.markelov.httphandlers.util.Responder;
import ru.nsu.fit.markelov.httphandlers.util.parsers.FormDataHandler;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserSubmitHandler extends RestHandler {

    private MainManager mainManager;
    private boolean create;

    public UserSubmitHandler(MainManager mainManager, boolean create) {
        this.mainManager = mainManager;
        this.create = create;
    }

    @Override
    protected void respond(HttpExchange exchange, Responder responder) throws IOException {
        Map<String, FormDataHandler.MultiPart> textParametersMap = new TreeMap<>();
        UserInput userInput = new UserInput();

        try {
            new FormDataHandler() {
                @Override
                public void handle(HttpExchange httpExchange, List<MultiPart> parts) {
                    for (MultiPart part : parts) {
                        if (part.type == PartType.FILE) {
                            if (part.name.equals("avatar")) {
                                userInput.getAvatarResource().setName(part.filename).setBytes(part.bytes);
                            }
                        } else {
                            textParametersMap.put(part.name, part);
                        }
                    }
                }
            }.handle(exchange);
        } catch (IOException e) {
            e.printStackTrace();
        }

        userInput.setName(textParametersMap.get("name").value);
        userInput.setPassword(textParametersMap.get("password").value);
        userInput.setRepeatPassword(textParametersMap.get("repeat_password").value);
        userInput.setType(textParametersMap.get("type").value);

        userInput.prepare();

        String error = userInput.getError();
        if (!error.isEmpty()) {
            throw new ProcessingException(error);
        }

        mainManager.submitUser(
            create,
            userInput.getName(),
            userInput.getPassword(),
            userInput.getType(),
            userInput.getAvatarResource()
        );

        responder.sendResponse("OK");
    }
}
