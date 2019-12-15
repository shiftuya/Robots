package ru.nsu.fit.markelov;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public class SimonsCoreClass implements SiteAPI {

    public String getListOfLobbies() {
        JSONObject lobby_1 = new JSONObject();
        lobby_1
                .put("lobby_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("avatar", "/images/person-icon.png")
                .put("host_name", "Simon")
                .put("level_icon", "/images/logo.png")
                .put("level_name", "Robot Wars")
                .put("level_difficulty", "Hard")
                .put("players", 1)
                .put("players_at_most", 2);

        // -----------------------------------------------------------------------------------------

        JSONObject lobby_2 = new JSONObject();
        lobby_2
                .put("lobby_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("avatar", "/images/person-icon.png")
                .put("host_name", "Vasily")
                .put("level_icon", "/images/logo.png")
                .put("level_name", "Vacuum Cleaner")
                .put("level_difficulty", "Medium")
                .put("players", 3)
                .put("players_at_most", 4);

        // -----------------------------------------------------------------------------------------

        JSONObject lobby_3 = new JSONObject();
        lobby_3
                .put("lobby_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("avatar", "/images/person-icon.png")
                .put("host_name", "Ivan")
                .put("level_icon", "/images/logo.png")
                .put("level_name", "Robot Wars")
                .put("level_difficulty", "Hard")
                .put("players", 1)
                .put("players_at_most", 2);

        // -----------------------------------------------------------------------------------------

        JSONArray lobbies = new JSONArray();
        lobbies.put(lobby_1);
        lobbies.put(lobby_2);
        lobbies.put(lobby_3);

        return new JSONObject().put("response", lobbies).toString();
    }

    public String getLevels() {
        JSONObject level_1 = new JSONObject();
        level_1
                .put("level_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("level_icon", "/images/labyrinth-icon.png")
                .put("level_name", "Labyrinth")
                .put("level_difficulty", "Easy")
                .put("level_type", "Single")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("min_players", 1)
                .put("max_players", 1);

        // -----------------------------------------------------------------------------------------

        JSONObject level_2 = new JSONObject();
        level_2
                .put("level_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("level_icon", "/images/vacuum-cleaner-icon.png")
                .put("level_name", "Vacuum Cleaner")
                .put("level_difficulty", "Medium")
                .put("level_type", "Multiplayer (2-4)")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("min_players", 2)
                .put("max_players", 4);

        // -----------------------------------------------------------------------------------------

        JSONObject level_3 = new JSONObject();
        level_3
                .put("level_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("level_icon", "/images/robot-wars-icon.png")
                .put("level_name", "Robot Wars")
                .put("level_difficulty", "Hard")
                .put("level_type", "Multiplayer (2)")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("min_players", 2)
                .put("max_players", 2);

        // -----------------------------------------------------------------------------------------

        JSONArray levels = new JSONArray();
        levels.put(level_1);
        levels.put(level_2);
        levels.put(level_3);

        return new JSONObject().put("response", levels).toString();
    }

    public String getSolutions(String userName) {
        JSONObject solution_1_attempt_1 = new JSONObject();
        solution_1_attempt_1
                .put("attempt_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("attempt_date", "03.11.2019")
                .put("attempt_result", false);

        JSONObject solution_1_attempt_2 = new JSONObject();
        solution_1_attempt_2
                .put("attempt_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("attempt_date", "05.11.2019")
                .put("attempt_result", true);

        JSONArray solution_1_attempts = new JSONArray();
        solution_1_attempts.put(solution_1_attempt_1);
        solution_1_attempts.put(solution_1_attempt_2);

        JSONObject solution_1 = new JSONObject();
        solution_1
                .put("level_icon", "/images/labyrinth-icon.png")
                .put("level_name", "Labyrinth")
                .put("level_difficulty", "Easy")
                .put("level_type", "Single")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("attempts", solution_1_attempts);

        // -----------------------------------------------------------------------------------------

        JSONObject solution_2_attempt_1 = new JSONObject();
        solution_2_attempt_1
                .put("attempt_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("attempt_date", "03.11.2019")
                .put("attempt_result", false);

        JSONObject solution_2_attempt_2 = new JSONObject();
        solution_2_attempt_2
                .put("attempt_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("attempt_date", "05.11.2019")
                .put("attempt_result", false);

        JSONObject solution_2_attempt_3 = new JSONObject();
        solution_2_attempt_3
                .put("attempt_id", ThreadLocalRandom.current().nextInt(1, 1000))
                .put("attempt_date", "06.11.2019")
                .put("attempt_result", false);

        JSONArray solution_2_attempts = new JSONArray();
        solution_2_attempts.put(solution_2_attempt_1);
        solution_2_attempts.put(solution_2_attempt_2);
        solution_2_attempts.put(solution_2_attempt_3);

        JSONObject solution_2 = new JSONObject();
        solution_2
                .put("level_icon", "/images/vacuum-cleaner-icon.png")
                .put("level_name", "Vacuum Cleaner")
                .put("level_difficulty", "Medium")
                .put("level_type", "Multiplayer (2-4)")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("attempts", solution_2_attempts);

        // -----------------------------------------------------------------------------------------

        JSONArray solution_3_attempts = new JSONArray();

        JSONObject solution_3 = new JSONObject();
        solution_3
                .put("level_icon", "/images/robot-wars-icon.png")
                .put("level_name", "Robot Wars")
                .put("level_difficulty", "Hard")
                .put("level_type", "Multiplayer (2)")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("attempts", solution_3_attempts);

        // -----------------------------------------------------------------------------------------

        JSONArray solutions = new JSONArray();
        solutions.put(solution_1);
        solutions.put(solution_2);
        solutions.put(solution_3);

        return new JSONObject().put("response", solutions).toString();
    }

    public String joinLobby(String userName, int lobbyID) {
        JSONObject player_1 = new JSONObject();
        player_1
                .put("avatar", "/images/person-icon.png")
                .put("user_name", "Vasily")
                .put("submitted", true);

        JSONObject player_2 = new JSONObject();
        player_2
                .put("avatar", "/images/person-icon.png")
                .put("user_name", "Simon")
                .put("submitted", true);

        JSONObject player_3 = new JSONObject();
        player_3
                .put("avatar", "/images/person-icon.png")
                .put("user_name", userName)
                .put("submitted", false);

        JSONArray players = new JSONArray();

        if (lobbyID == 99999) {
            players.put(player_3);
        } else {
            players.put(player_1);
            players.put(player_2);
            players.put(player_3);
        }

        // -----------------------------------------------------------------------------------------

        JSONObject lobby = new JSONObject();
        lobby
                .put("lobby_id", lobbyID)
                .put("level_icon", "/images/vacuum-cleaner-icon.png")
                .put("level_name", "Vacuum Cleaner")
                .put("players", 3)
                .put("players_at_most", 4)
                .put("level_difficulty", "Hard")
                .put("level_type", "Mulriplayer")
                .put("description", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of...")
                .put("rules", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .put("goal", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.")
                .put("players_list", players);

        // -----------------------------------------------------------------------------------------

        return new JSONObject().put("response", lobby).toString();
    }

    public String createLobby(String userName, int levelID) {
        // create new lobby using "levelID", then get its id
        int lobbyID = 99999;

        return joinLobby(userName, lobbyID);
    }
}
