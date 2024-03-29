package ru.nsu.fit.markelov;

import com.sun.net.httpserver.HttpServer;
import ru.nsu.fit.markelov.httphandlers.handlers.CommonHttpHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.code.CodeEditHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.collections.LevelsGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.collections.LobbiesGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.collections.SimulatorsGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.collections.SolutionsGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.collections.UsersGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.level.LevelDeleteHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.level.LevelGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.level.LevelSubmitHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby.LobbyCreateHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby.LobbyJoinHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby.LobbyLeaveHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby.LobbyReturnHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.lobby.LobbySubmitHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.log.LogInHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.log.LogOutHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.simulationresult.LogGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.simulationresult.ScriptGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.simulationresult.SimulationResultGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.simulationresult.SimulationResultIsReadyHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.simulator.SimulatorAddHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.simulator.SimulatorDeleteHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.user.UserBlockHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.user.UserDeleteHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.user.UserGetHandler;
import ru.nsu.fit.markelov.httphandlers.handlers.rest.user.UserSubmitHandler;
import ru.nsu.fit.markelov.interfaces.client.MainManager;
import ru.nsu.fit.markelov.mainmanager.MainManagerWithDatabase;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class MainServer {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManagerWithDatabase();

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(80), 0);

        server.createContext("/", new CommonHttpHandler(mainManager, null));

        server.createContext("/login", new CommonHttpHandler(mainManager, "login"));
        server.createContext("/list_of_lobbies", new CommonHttpHandler(mainManager, "list_of_lobbies"));
        server.createContext("/choose_level", new CommonHttpHandler(mainManager, "choose_level"));
        server.createContext("/my_solutions", new CommonHttpHandler(mainManager, "my_solutions"));
        server.createContext("/users", new CommonHttpHandler(mainManager, "users"));
        server.createContext("/user", new CommonHttpHandler(mainManager, "user", "user.get"));
        server.createContext("/user_editor", new CommonHttpHandler(mainManager, "user_editor", "user.get"));
        server.createContext("/levels", new CommonHttpHandler(mainManager, "levels"));
        server.createContext("/level_editor", new CommonHttpHandler(mainManager, "level_editor", "level.get"));
        server.createContext("/simulators", new CommonHttpHandler(mainManager, "simulators"));
        server.createContext("/options", new CommonHttpHandler(mainManager, "options"));
        server.createContext("/lobby", new CommonHttpHandler(mainManager, "lobby", "lobby.return"));
        server.createContext("/code_editor", new CommonHttpHandler(mainManager, "code_editor", "code.edit"));
        server.createContext("/simulation_result", new CommonHttpHandler(mainManager, "simulation_result", "simulation_result.get"));

        server.createContext("/api/method/sign.login", new LogInHandler(mainManager));
        server.createContext("/api/method/sign.logout", new LogOutHandler(mainManager));

        server.createContext("/api/method/lobbies.get", new LobbiesGetHandler(mainManager));
        server.createContext("/api/method/solutions.get", new SolutionsGetHandler(mainManager));
        server.createContext("/api/method/users.get", new UsersGetHandler(mainManager));
        server.createContext("/api/method/levels.get", new LevelsGetHandler(mainManager));
        server.createContext("/api/method/simulators.get", new SimulatorsGetHandler(mainManager));

        server.createContext("/api/method/lobby.join", new LobbyJoinHandler(mainManager));
        server.createContext("/api/method/lobby.create", new LobbyCreateHandler(mainManager));
        server.createContext("/api/method/lobby.leave", new LobbyLeaveHandler(mainManager));
        server.createContext("/api/method/lobby.return", new LobbyReturnHandler(mainManager));
        server.createContext("/api/method/lobby.submit", new LobbySubmitHandler(mainManager));

        server.createContext("/api/method/code.edit", new CodeEditHandler(mainManager));

        server.createContext("/api/method/simulation_result.is_ready", new SimulationResultIsReadyHandler(mainManager));
        server.createContext("/api/method/simulation_result.get", new SimulationResultGetHandler(mainManager));
        server.createContext("/api/method/log.get", new LogGetHandler(mainManager));
        server.createContext("/api/method/script.get", new ScriptGetHandler(mainManager));

        server.createContext("/api/method/user.get", new UserGetHandler(mainManager));
        server.createContext("/api/method/user.block", new UserBlockHandler(mainManager));
        server.createContext("/api/method/user.delete", new UserDeleteHandler(mainManager));
        server.createContext("/api/method/user.create", new UserSubmitHandler(mainManager, true));
        server.createContext("/api/method/user.edit", new UserSubmitHandler(mainManager, false));

        server.createContext("/api/method/level.get", new LevelGetHandler(mainManager));
        server.createContext("/api/method/level.submit", new LevelSubmitHandler(mainManager));
        server.createContext("/api/method/level.delete", new LevelDeleteHandler(mainManager));

        server.createContext("/api/method/simulator.add", new SimulatorAddHandler(mainManager));
        server.createContext("/api/method/simulator.delete", new SimulatorDeleteHandler(mainManager));

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }
}
