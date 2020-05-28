package ru.nsu.fit.markelov.httphandlers.util;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.Pair;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonPacker {

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static JSONArray packUsers(Iterable<User> users) {
        JSONArray jsonLevels = new JSONArray();

        for (User user : users) {
            jsonLevels.put(packUser(user));
        }

        return jsonLevels;
    }

    public static JSONObject packUser(User user) {
        JSONObject jsonUser = new JSONObject();
        jsonUser
            .put("avatar", user.getAvatarAddress())
            .put("name", user.getName())
            .put("type", user.getType())
            .put("last_active", user.getLastActive() == null ? "Never" :
                DateFormatter.formatLastActive(user.getLastActive().getTime()))
            .put("is_blocked", user.isBlocked());

        return jsonUser;
    }

    public static JSONObject packUserInfo(JSONObject jsonUser, JSONArray jsonSolutions) {
        return new JSONObject()
            .put("info", jsonUser)
            .put("solutions", jsonSolutions);
    }

    public static JSONArray packLevels(Iterable<Level> levels) {
        JSONArray jsonLevels = new JSONArray();

        for (Level level : levels) {
            JSONObject jsonLevel = new JSONObject();
            jsonLevel
                .put("level_id", level.getId())
                .put("level_icon", level.getIconAddress())
                .put("level_name", level.getName())
                .put("level_difficulty", level.getDifficulty())
                .put("level_type", level.getType())
                .put("description", level.getDescription())
                .put("rules", level.getRules())
                .put("goal", level.getGoal())
                .put("min_players", level.getMinPlayers())
                .put("max_players", level.getMaxPlayers());

            jsonLevels.put(jsonLevel);
        }

        return jsonLevels;
    }

    // у этого левела другая схема json - его нельзя использовать в packLevels
    public static JSONObject packLevel(Level level) {
        JSONObject jsonLevel = new JSONObject();
        jsonLevel
            .put("level_id", level.getId())
            .put("level_name", level.getName())
            .put("level_difficulty", level.getDifficulty())
            .put("level_type", level.getType())
            .put("description", level.getDescription())
            .put("rules", level.getRules())
            .put("goal", level.getGoal())
            .put("min_players", level.getMinPlayers())
            .put("max_players", level.getMaxPlayers())
            .put("code", level.getCode())
            .put("language", level.getLanguage());

        return jsonLevel;
    }

    public static JSONArray packSimulators(Iterable<String> urls) {
        JSONArray jsonUrls = new JSONArray();

        for (String url : urls) {
            JSONObject jsonUrl = new JSONObject();
            jsonUrl
                .put("url", url);

            jsonUrls.put(jsonUrl);
        }

        return jsonUrls;
    }

    public static JSONArray packSolutions(String username, Map<Level, Iterable<SimulationResult>> solutions) {
        JSONArray jsonSolutions = new JSONArray();

        for (Level level : solutions.keySet()) {
            JSONArray jsonAttempts = new JSONArray();

            for (SimulationResult attempt : solutions.get(level)) {
                JSONObject jsonAttempt = new JSONObject();

                jsonAttempt
                    .put("attempt_id", attempt.getId())
                    .put("attempt_date", new SimpleDateFormat(DATE_FORMAT).format(attempt.getDate()))
                    .put("attempt_result", attempt.isSuccessful(username));

                jsonAttempts.put(jsonAttempt);
            }

            JSONObject jsonSolution = new JSONObject();

            jsonSolution
                .put("level_icon", level.getIconAddress())
                .put("level_name", level.getName())
                .put("level_difficulty", level.getDifficulty())
                .put("level_type", level.getType())
                .put("description", level.getDescription())
                .put("rules", level.getRules())
                .put("goal", level.getGoal())
                .put("attempts", jsonAttempts);

            jsonSolutions.put(jsonSolution);
        }

        return jsonSolutions;
    }

    public static JSONArray packLobbies(Iterable<Lobby> lobbies) {
        JSONArray jsonLobbies = new JSONArray();

        for (Lobby lobby : lobbies) {
            JSONObject jsonLobby = new JSONObject();

            jsonLobby
                    .put("lobby_id", lobby.getId())
                    .put("avatar", lobby.getUsers().get(0).getKey().getAvatarAddress())
                    .put("host_name", lobby.getUsers().get(0).getKey().getName())
                    .put("level_icon", lobby.getLevel().getIconAddress())
                    .put("level_name", lobby.getLevel().getName())
                    .put("level_difficulty", lobby.getLevel().getDifficulty())
                    .put("players", lobby.getCurrentPlayersAmount())
                    .put("players_at_most", lobby.getAcceptablePlayersAmount());

            jsonLobbies.put(jsonLobby);
        }

        return jsonLobbies;
    }

    // у этого лобби другая схема json - его нельзя использовать в packLobbies
    public static JSONObject packLobby(Lobby lobby) {
        JSONArray jsonPlayers = new JSONArray();

        for (Pair<User, Boolean> user : lobby.getUsers()) {
            JSONObject jsonPlayer = new JSONObject();

            jsonPlayer
                    .put("avatar", user.getKey().getAvatarAddress())
                    .put("user_name", user.getKey().getName())
                    .put("submitted", user.getValue());

            jsonPlayers.put(jsonPlayer);
        }

        JSONObject jsonLobby = new JSONObject();
        jsonLobby
                .put("lobby_id", lobby.getId())
                .put("level_icon", lobby.getLevel().getIconAddress())
                .put("level_name", lobby.getLevel().getName())
                .put("players", lobby.getCurrentPlayersAmount())
                .put("players_at_most", lobby.getAcceptablePlayersAmount())
                .put("level_difficulty", lobby.getLevel().getDifficulty())
                .put("level_type", lobby.getLevel().getType())
                .put("description", lobby.getLevel().getDescription())
                .put("rules", lobby.getLevel().getRules())
                .put("goal", lobby.getLevel().getGoal())
                .put("players_list", jsonPlayers);

        return jsonLobby;
    }

    public static JSONObject packCompileResult(CompileResult compileResult) {
        JSONObject jsonCompileResult = new JSONObject();
        jsonCompileResult
                .put("simulated", compileResult.isSimulated())
                .put("compiled", compileResult.isCompiled())
                .put("message", compileResult.getMessage());

        return jsonCompileResult;
    }

    public static JSONObject packCode(String code) {
        JSONObject jsonCode = new JSONObject();
        jsonCode.put("code", (code != null) ? code : "");

        return jsonCode;
    }

    public static JSONObject packSimulationResultReadiness(boolean isSimulationFinished) {
        JSONObject jsonSimulationResultReadiness = new JSONObject();
        jsonSimulationResultReadiness
                .put("simulation_finished", isSimulationFinished);

        return jsonSimulationResultReadiness;
    }

    public static JSONObject packSimulationResult(SimulationResult simulationResult) {
        JSONArray jsonSimulationResults = new JSONArray();

        for (User user : simulationResult.getUsers()) {
            JSONObject jsonSimulationResult = new JSONObject();

            jsonSimulationResult
                .put("avatar-icon", user.getAvatarAddress())
                .put("username", user.getName())
                .put("result", simulationResult.isSuccessful(user.getName()));

            jsonSimulationResults.put(jsonSimulationResult);
        }

        return new JSONObject()
            .put("id", simulationResult.getId())
            .put("date", new SimpleDateFormat(DATE_FORMAT).format(simulationResult.getDate()))
            .put("users", jsonSimulationResults)
            .put("playback", packPlayback(simulationResult.getPlayback()));
    }

    public static JSONObject packPlayback(Playback playback) {
        JSONArray jsonBindings = new JSONArray();
        for (Map.Entry<String, Integer> entry : playback.getRobots().entrySet()) {
            JSONObject jsonBinding = new JSONObject();
            jsonBinding
                .put("user", entry.getKey())
                .put("id", entry.getValue());

            jsonBindings.put(jsonBinding);
        }

        JSONArray jsonGameObjectsStates = new JSONArray();
        for (List<GameObjectState> gameObjectStates : playback.getGameObjectStates()) {
            JSONArray jsonGameObjectStates = new JSONArray();
            for (GameObjectState gameObjectState : gameObjectStates) {
                JSONArray jsonSensors = new JSONArray();
                for (Map.Entry<String, String> entry : gameObjectState.getSensorValues().entrySet()) {
                    JSONObject jsonSensor = new JSONObject();
                    jsonSensor
                        .put("sensor", entry.getKey())
                        .put("value", entry.getValue());

                    jsonSensors.put(jsonSensor);
                }

                JSONObject jsonGameObjectState = new JSONObject();
                jsonGameObjectState
                    .put("startingFrame", gameObjectState.getStartingFrame())
                    .put("endingFrame", gameObjectState.getEndingFrame())
                    .put("position", new JSONArray()
                        .put(gameObjectState.getPosition().getX())
                        .put(gameObjectState.getPosition().getY())
                        .put(gameObjectState.getPosition().getZ()))
                    .put("dimension", new JSONArray()
                        .put(gameObjectState.getDimension().getX())
                        .put(gameObjectState.getDimension().getY())
                        .put(gameObjectState.getDimension().getZ()))
                    .put("rotation", new JSONArray()
                        .put(gameObjectState.getRotation().getX())
                        .put(gameObjectState.getRotation().getY())
                        .put(gameObjectState.getRotation().getZ()))
                    .put("color", gameObjectState.getColor())
                    .put("sensors", jsonSensors);

                jsonGameObjectStates.put(jsonGameObjectState);
            }
            jsonGameObjectsStates.put(jsonGameObjectStates);
        }

        JSONObject jsonCamera = new JSONObject();
        jsonCamera
            .put("position", new JSONArray()
                .put(playback.getCamera().getPosition().getX())
                .put(playback.getCamera().getPosition().getY())
                .put(playback.getCamera().getPosition().getZ()))
            .put("renderDistance", playback.getCamera().getRenderDistance())
            .put("scrollSpeed", playback.getCamera().getScrollSpeed());

        JSONObject jsonGround = new JSONObject();
        jsonGround
            .put("size", playback.getGround().getSize())
            .put("color", playback.getGround().getColor())
            .put("gridDivisions", playback.getGround().getGridDivisions())
            .put("gridColor", playback.getGround().getGridColor())
            .put("gridCenterLineColor", playback.getGround().getGridCenterLineColor())
            .put("opacity", playback.getGround().getGridOpacity());

        return new JSONObject()
            .put("framesCount", playback.getFramesCount())
            .put("bindings", jsonBindings)
            .put("gameObjectsStates", jsonGameObjectsStates)
            .put("camera", jsonCamera)
            .put("backgroundColor", playback.getBackgroundColor())
            .put("ground", jsonGround);
    }
}
