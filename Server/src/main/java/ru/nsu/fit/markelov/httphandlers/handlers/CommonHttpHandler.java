package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonHttpHandler implements HttpHandler {

    private static final String RESOURCES_FOLDER = "src/main/resources/";
    private static final String HTML_PAGE = "index.html";

    private final String context;
    private final String ajaxQuery;

    public CommonHttpHandler(String context) {
        this(context, null);
    }

    public CommonHttpHandler(String context, String ajaxQuery) {
        this.context = context;
        this.ajaxQuery = ajaxQuery;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String _context = context;
        String _ajaxQuery = ajaxQuery;

        try (OutputStream oStream = exchange.getResponseBody()) {
            String uri = exchange.getRequestURI().toString();

            if (_ajaxQuery != null) {
                String params = new UriParametersParser(uri).getParams();
                if (params == null) {
                    _ajaxQuery = null;
                } else {
                    _context += "?" + params;
                    _ajaxQuery += "?" + params;
                }
            }

            String fileName = RESOURCES_FOLDER + uri;
            try {
                Path path = Paths.get(fileName);
                if (Files.isRegularFile(path)) {
                    byte[] bytes = Files.readAllBytes(path);
                    exchange.sendResponseHeaders(200, bytes.length);
                    oStream.write(bytes);

                    return;
                }
            } catch (InvalidPathException e) {}

            if (uri.equals("/")) {
                _context = "login";
                uri = "/login";
            }

            byte[] page = Files.readAllBytes(Paths.get(RESOURCES_FOLDER + HTML_PAGE));
            int pos = getPositionOfSecondEnclosingAngleBracketFromEnd(page);

            int responseCode = 200;
            if (!uri.equals("/" + _context)) {
                responseCode = 404;
                _context = "404";
            }

            String contextScript = "var initialContext = \"" + _context + "\";";
            String ajaxQueryScript = "var initialAjaxQuery = " + (_ajaxQuery != null ? "\"" + _ajaxQuery + "\"" : "undefined") + ";";
            String script = "    <script>" + contextScript + ajaxQueryScript + "</script>\n    </body>\n</html>\n";

            exchange.sendResponseHeaders(responseCode, pos + script.length());
            oStream.write(page, 0, pos);
            oStream.write(script.getBytes());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private int getPositionOfSecondEnclosingAngleBracketFromEnd(byte[] bytes) {
        int i, j = 0;
        for (i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] == 60 && j++ == 1) {
                return i;
            }
        }

        return -1;
    }
}
