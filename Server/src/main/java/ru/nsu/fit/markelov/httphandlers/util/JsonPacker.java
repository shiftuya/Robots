package ru.nsu.fit.markelov.httphandlers.util;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Lobby;
import ru.nsu.fit.markelov.interfaces.client.Player;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonPacker {

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static String packLevels(List<Level> levels) {
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

        return new JSONObject().put("response", jsonLevels).toString();
    }

    // у этого левела другая схема json - его нельзя использовать в packLevels
    public static String packLevel(Level level) {
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

        return new JSONObject().put("response", jsonLevel).toString();
    }

    public static String packLevelDelete(boolean deleted) {
        JSONObject jsonDeleted = new JSONObject();
        jsonDeleted
            .put("deleted", deleted);

        return new JSONObject().put("response", jsonDeleted).toString();
    }

    public static String packSimulators(List<String> urls) {
        JSONArray jsonUrls = new JSONArray();

        for (String url : urls) {
            JSONObject jsonUrl = new JSONObject();
            jsonUrl
                .put("url", url);

            jsonUrls.put(jsonUrl);
        }

        return new JSONObject().put("response", jsonUrls).toString();
    }

    public static String packSimulatorAdd(boolean added) {
        JSONObject jsonDeleted = new JSONObject();
        jsonDeleted
            .put("added", added);

        return new JSONObject().put("response", jsonDeleted).toString();
    }

    public static String packSimulatorDelete(boolean deleted) {
        JSONObject jsonDeleted = new JSONObject();
        jsonDeleted
            .put("deleted", deleted);

        return new JSONObject().put("response", jsonDeleted).toString();
    }

    public static String packSolutions(String username, Map<Level, List<SimulationResult>> solutions) {
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

        return new JSONObject().put("response", jsonSolutions).toString();
    }

    public static String packLobbies(List<Lobby> lobbies) {
        JSONArray jsonLobbies = new JSONArray();

        for (Lobby lobby : lobbies) {
            JSONObject jsonLobby = new JSONObject();

            jsonLobby
                    .put("lobby_id", lobby.getId())
                    .put("avatar", lobby.getHostAvatarAddress())
                    .put("host_name", lobby.getHostName())
                    .put("level_icon", lobby.getLevel().getIconAddress())
                    .put("level_name", lobby.getLevel().getName())
                    .put("level_difficulty", lobby.getLevel().getDifficulty())
                    .put("players", lobby.getCurrentPlayersAmount())
                    .put("players_at_most", lobby.getAcceptablePlayersAmount());

            jsonLobbies.put(jsonLobby);
        }

        return new JSONObject().put("response", jsonLobbies).toString();
    }

    // у этого лобби другая схема json - его нельзя использовать в packLobbies
    public static String packLobby(Lobby lobby) {
        JSONArray jsonPlayers = new JSONArray();

        for (Player player : lobby.getPlayers()) {
            JSONObject jsonPlayer = new JSONObject();

            jsonPlayer
                    .put("avatar", player.getAvatarAddress())
                    .put("user_name", player.getName())
                    .put("submitted", player.isSubmitted());

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

        return new JSONObject().put("response", jsonLobby).toString();
    }

    public static String packLeavingLobby(boolean userRemovedFromLobby) {
        return new JSONObject().put("response", new JSONObject().put("successful", userRemovedFromLobby)).toString();
    }

    public static String packCompileResult(CompileResult compileResult) {
        JSONObject jsonCompileResult = new JSONObject();
        jsonCompileResult
                .put("simulated", compileResult.isSimulated())
                .put("compiled", compileResult.isCompiled())
                .put("message", compileResult.getMessage());

        return new JSONObject().put("response", jsonCompileResult).toString();
    }

    public static String packCode(String code) {
        JSONObject jsonCode = new JSONObject();
        jsonCode.put("code", (code != null) ? code : "");

        return new JSONObject().put("response", jsonCode).toString();
    }

    public static String packSimulationResultReadiness(boolean isSimulationFinished) {
        JSONObject jsonSimulationResultReadiness = new JSONObject();
        jsonSimulationResultReadiness
                .put("simulation_finished", isSimulationFinished);

        return new JSONObject().put("response", jsonSimulationResultReadiness).toString();
    }

    public static String packSimulationResult(SimulationResult simulationResult, String username) {
        JSONObject jsonSimulationResult = new JSONObject();
        jsonSimulationResult
                .put("simulation_result_id", simulationResult.getId())
                .put("simulation_result_date", new SimpleDateFormat(DATE_FORMAT).format(simulationResult.getDate()))
                .put("simulation_result_status", simulationResult.isSuccessful(username))
                .put("simulation_result_log", simulationResult.getLog(username));

        return new JSONObject().put("response", jsonSimulationResult).toString();
    }

    public static String packLoggingIn(boolean loggedIn) {
        JSONObject jsonLoggingIn = new JSONObject();
        jsonLoggingIn
                .put("logged_in", loggedIn)
                .put("message", loggedIn ? "You are successfully logged in." : "Error: You logged in earlier.");

        return new JSONObject().put("response", jsonLoggingIn).toString();
    }

    public static String packLoggingOut(boolean loggedOut) {
        JSONObject jsonLoggingOut = new JSONObject();
        jsonLoggingOut
                .put("logged_out", loggedOut)
                .put("message", loggedOut ? "You are successfully logged out." : "Error: You haven't logged in yet.");

        return new JSONObject().put("response", jsonLoggingOut).toString();
    }
}
