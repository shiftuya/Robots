package ru.nsu.fit.markelov.httphandlers.util.parsers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class FormDataParser {

    Map<String, FormDataHandler.MultiPart> textParametersMap = new TreeMap<>();

    public FormDataParser(HttpExchange exchange) {
        try {
            new FormDataHandler() {
                @Override
                public void handle(HttpExchange httpExchange, List<MultiPart> parts) {
                    for (MultiPart part : parts) {
                        if (part.type == PartType.FILE) {
                            addFiles(part);
                        } else {
                            textParametersMap.put(part.name, part);
                        }
                    }
                }
            }.handle(exchange);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void addFiles(FormDataHandler.MultiPart part);

    public String getFieldValue(String key) {
        if (textParametersMap.containsKey(key)) {
            return textParametersMap.get(key).value;
        } else {
            return null;
        }
    }
}
