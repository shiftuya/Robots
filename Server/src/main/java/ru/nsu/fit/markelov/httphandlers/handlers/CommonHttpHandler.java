package ru.nsu.fit.markelov.httphandlers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.CookieHandler;
import ru.nsu.fit.markelov.httphandlers.util.parsers.UriParametersParser;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.interfaces.client.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonHttpHandler implements HttpHandler {

    private static final String RESOURCES_FOLDER = "src/main/resources/";
    private static final String HTML_PAGE = "index.html";

    private final MainManager mainManager;
    private final String context;
    private final String ajaxQuery;

    public CommonHttpHandler(MainManager mainManager, String context) {
        this(mainManager, context, null);
    }

    public CommonHttpHandler(MainManager mainManager, String context, String ajaxQuery) {
        this.mainManager = mainManager;
        this.context = context;
        this.ajaxQuery = ajaxQuery;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (OutputStream oStream = exchange.getResponseBody()) {
            String uri = exchange.getRequestURI().toString();

            try {
                String fileName = RESOURCES_FOLDER + uri;
                Path path = Paths.get(fileName);
                if (Files.isRegularFile(path)) {
                    byte[] bytes = Files.readAllBytes(path);
                    exchange.sendResponseHeaders(200, bytes.length);
                    oStream.write(bytes);

                    return;
                }
            } catch (InvalidPathException ignored) {}

            String context = this.context;
            String ajaxQuery = this.ajaxQuery;
            if (ajaxQuery != null) {
                String params = new UriParametersParser(uri).getParams();
                if (params == null) {
                    ajaxQuery = null;
                } else {
                    context += "?" + params;
                    ajaxQuery += "?" + params;
                }
            }

            String userName;
            String userType;
            try {
                CookieHandler cookieHandler = new CookieHandler(exchange);
                //cookieHandler.printCookieDEBUG();
                userName = mainManager.getUserName(cookieHandler.getCookie());
                userType = mainManager.getUserType(cookieHandler.getCookie()).toString();
            } catch (ProcessingException e) {
                //System.out.println(e.getMessage());
                userName = null;
                userType = null;
            }

            if (uri.equals("/") || userName == null) {
                context = "login";
                uri = "/login";
            }

            byte[] page = Files.readAllBytes(Paths.get(RESOURCES_FOLDER + HTML_PAGE));
            int pos = getPositionOfSecondEnclosingAngleBracketFromEnd(page);

            int responseCode = 200;
            if (!uri.equals("/" + context) || !hasPermission(userType, context)) {
                responseCode = 404;
                context = "404";
            }

            String userNameScript = getScript("initialUserName", userName);
            String userTypeScript = getScript("initialUserType", userType);
            String contextScript = getScript("initialContext", context);
            String ajaxQueryScript = getScript("initialAjaxQuery", ajaxQuery);

            String script = "    <script>" + userNameScript + userTypeScript + contextScript + ajaxQueryScript + "</script>\n    </body>\n</html>\n";

            exchange.sendResponseHeaders(responseCode, pos + script.length());
            oStream.write(page, 0, pos);
            oStream.write(script.getBytes());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private boolean hasPermission(String userType, String context) {
        if (userType == null) {
            return true;
        }

        if (userType.equals(User.UserType.Teacher.toString())) {
            return !context.startsWith("simulators");
        } else if (userType.equals(User.UserType.Student.toString())) {
            return
                !context.startsWith("users") &&
                !context.startsWith("user") &&
                !context.startsWith("user_editor") &&
                !context.startsWith("levels") &&
                !context.startsWith("level_editor") &&
                !context.startsWith("simulators");
        }

        return true;
    }

    private String getScript(String varName, String variable) {
        return "var " + varName + " = " + (variable != null ? "\"" + variable + "\"" : "undefined") + ";";
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
