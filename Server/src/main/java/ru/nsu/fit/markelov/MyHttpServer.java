package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpServer;
import ru.nsu.fit.markelov.http_handlers.ApiHandler;
import ru.nsu.fit.markelov.http_handlers.CommonHttpHandler;
import ru.nsu.fit.markelov.managers.MainManager;
import ru.nsu.fit.markelov.managers.MainManagerClass;

import java.net.InetSocketAddress;

public class MyHttpServer {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManagerClass();

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(1337), 0);

        server.createContext("/", new CommonHttpHandler());
        server.createContext("/api/method", new ApiHandler(mainManager));

        server.setExecutor(null);
        server.start();
    }

    // ----- for DEBUG only -----

    /*public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(1337), 0);

        HttpContext context = server.createContext("/", new EchoHandler());
        context.setAuthenticator(new Auth());

        server.setExecutor(null);
        server.start();
    }

    static class EchoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try (OutputStream oStream = exchange.getResponseBody()) {
                StringBuilder builder = new StringBuilder();

                builder.append("<h1>URI: ").append(exchange.getRequestURI()).append("</h1>");

                Headers headers = exchange.getRequestHeaders();
                for (String header : headers.keySet()) {
                    builder.append("<p>").append(header).append("=")
                            .append(headers.getFirst(header)).append("</p>");
                }

                byte[] bytes = builder.toString().getBytes();
                exchange.sendResponseHeaders(200, bytes.length);

                oStream.write(bytes);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            if ("/forbidden".equals(httpExchange.getRequestURI().toString()))
                return new Failure(403);
            else
                return new Success(new HttpPrincipal("c0nst", "realm"));
        }
    }*/
}
