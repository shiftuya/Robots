package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpServer;
import ru.nsu.fit.markelov.httphandlers.handlers.CodeEditHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.CommonHttpHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LevelSubmitHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LevelsGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LobbiesGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LobbyCreateHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LobbyJoinHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LobbyLeaveHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LobbyReturnHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LobbySubmitHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LogInHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.LogOutHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.SimulationResultGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.SimulationResultIsReadyHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.SolutionsGetHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.mainmanager.MainManager1;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class MainServer {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManager1();

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(5051), 0);

        server.createContext("/", new CommonHttpHandler());

        server.createContext("/api/method/sign.login", new LogInHandler(mainManager));
        server.createContext("/api/method/sign.logout", new LogOutHandler(mainManager));

        server.createContext("/api/method/lobbies.get", new LobbiesGetHandler(mainManager));
        server.createContext("/api/method/levels.get", new LevelsGetHandler(mainManager));
        server.createContext("/api/method/solutions.get", new SolutionsGetHandler(mainManager));

        server.createContext("/api/method/lobby.join", new LobbyJoinHandler(mainManager));
        server.createContext("/api/method/lobby.create", new LobbyCreateHandler(mainManager));
        server.createContext("/api/method/lobby.leave", new LobbyLeaveHandler(mainManager));
        server.createContext("/api/method/lobby.return", new LobbyReturnHandler(mainManager));
        server.createContext("/api/method/lobby.submit", new LobbySubmitHandler(mainManager));

        server.createContext("/api/method/code.edit", new CodeEditHandler(mainManager));

        server.createContext("/api/method/simulation_result.is_ready", new SimulationResultIsReadyHandler(mainManager));
        server.createContext("/api/method/simulation_result.get", new SimulationResultGetHandler(mainManager));

        server.createContext("/api/method/level.submit", new LevelSubmitHandler(mainManager));

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }
}
