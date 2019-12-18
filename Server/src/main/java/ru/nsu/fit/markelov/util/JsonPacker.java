package ru.nsu.fit.markelov.util;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.Attempt;
import ru.nsu.fit.markelov.interfaces.Level;
import ru.nsu.fit.markelov.interfaces.Lobby;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.Solution;

import java.util.List;

public class JsonPacker {

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

    public static String packSolutions(List<Solution> solutions) {
        JSONArray jsonSolutions = new JSONArray();

        for (Solution solution : solutions) {
            JSONArray jsonAttempts = new JSONArray();

            for (Attempt attempt : solution.getAttempts()) {
                JSONObject jsonAttempt = new JSONObject();

                jsonAttempt
                        .put("attempt_id", attempt.getId())
                        .put("attempt_date", attempt.getDate())
                        .put("attempt_result", attempt.isSuccessful());

                jsonAttempts.put(jsonAttempt);
            }

            JSONObject jsonSolution = new JSONObject();

            jsonSolution
                    .put("level_icon", solution.getLevel().getIconAddress())
                    .put("level_name", solution.getLevel().getName())
                    .put("level_difficulty", solution.getLevel().getDifficulty())
                    .put("level_type", solution.getLevel().getType())
                    .put("description", solution.getLevel().getDescription())
                    .put("rules", solution.getLevel().getRules())
                    .put("goal", solution.getLevel().getGoal())
                    .put("attempts", jsonAttempts);

            jsonSolutions.put(jsonSolution);
        }

        return new JSONObject().put("response", jsonSolutions).toString();
    }
}
