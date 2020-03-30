package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonHttpHandler implements HttpHandler {

    private static final String RESOURCES_FOLDER = "src/main/resources/";
    private static final String HTML_PAGE = "index.html";

    private String context;

    public CommonHttpHandler(String context) {
        this.context = context;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String uri = exchange.getRequestURI().toString();

            String fileName = RESOURCES_FOLDER + uri;
            Path path = Paths.get(fileName);

            if (Files.isRegularFile(path)) {
                byte[] bytes = Files.readAllBytes(path);
                exchange.sendResponseHeaders(200, bytes.length);
                oStream.write(bytes);

                return;
            }

            if (uri.equals("/")) {
                context = "login";
                uri = "/login";
            }

            byte[] page = Files.readAllBytes(Paths.get(RESOURCES_FOLDER + HTML_PAGE));
            int pos = getPositionOfSecondEnclosingAngleBracketFromEnd(page);

            int responseCode = 200;
            if (!uri.equals("/" + context)) {
                responseCode = 404;
                context = "404";
            }

            String contextScript = "    <script>var initialContext = \"" + context + "\";</script>\n    </body>\n</html>\n";

            exchange.sendResponseHeaders(responseCode, pos + contextScript.length());
            oStream.write(page, 0, pos);
            oStream.write(contextScript.getBytes());
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
