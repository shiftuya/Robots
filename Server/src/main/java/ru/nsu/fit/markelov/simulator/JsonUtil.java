package ru.nsu.fit.markelov.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Playback;
import ru.nsu.fit.markelov.mainmanager.GameObjectState1;
import ru.nsu.fit.markelov.mainmanager.Playback1;
import ru.nsu.fit.markelov.mainmanager.SimulationResult1;
import ru.nsu.fit.markelov.mainmanager.Vec3;

import java.util.*;

class JsonUtil {

    static String formJSON(String levelId, ArrayList<String> solutionList) {
        JSONObject jsObj = new JSONObject();
        jsObj.put("level", levelId);
        jsObj.put("solutions", solutionList);
        return jsObj.toString();
    }

    static SimulationResult1 parseSimResponse(
            int id, String jsonStr, ArrayList<Map.Entry<User, String>> entryList) {
        JSONObject jsObj = new JSONObject(jsonStr);
        HashMap<String, Boolean> passed = new HashMap<>();
        HashMap<String, String> logs = new HashMap<>();
        Playback playback = null;
        try {
            if (jsObj.getBoolean("timeout") || jsObj.getBoolean("broken")) {
                for (int i = 0; i < entryList.size(); i++) {
                    for (Map.Entry<User, String> entry : entryList) {
                        passed.put(entry.getKey().getName(), false);
                    }
                }
            } else {
                JSONArray arr = jsObj.getJSONArray("results");
                for (int i = 0; i < arr.length(); i++) {
                    passed.put(entryList.get(i).getKey().getName(), arr.getBoolean(i));
                }
            }
            JSONArray logsJson = jsObj.getJSONArray("logs");
            for (int i = 0; i < logsJson.length(); i++) {
                logs.put(entryList.get(i).getKey().getName(), logsJson.getString(i));
            }
            JSONObject playbackJson = jsObj.getJSONObject("playback");
            HashMap<String, Integer> users = new HashMap<>();
            for (int i = 0; i < entryList.size(); i++) {
                users.put(entryList.get(i).getKey().getName(), i);
            }
            playback = parsePlayback(playbackJson, users);

        } catch (Exception e) {
            for (int i = 0; i < entryList.size(); i++) {
                e.printStackTrace();
                passed.put(entryList.get(i).getKey().getName(), false);
            }
        }
        return new SimulationResult1(id, passed, logs, playback, new Date());
    }

    static Playback1 parsePlayback(JSONObject playbackJson, Map<String, Integer> users) {
        Playback1 playback;
        int frames;

        List<List<GameObjectState>> outerRes = new ArrayList<>();
        JSONArray outerList = playbackJson.getJSONArray("gameObjectStates");
        for (int i = 0; i < outerList.length(); i++) {
            JSONArray innerList = outerList.getJSONArray(i);
            List<GameObjectState> innerRes = new ArrayList<>();
            for (int j = 0; j < innerList.length(); j++) {
                innerRes.add(parseGameObjectState(innerList.getJSONObject(j)));
            }
            outerRes.add(innerRes);
        }
        frames = playbackJson.getInt("framesCount");


        playback = new Playback1(frames, outerRes, users);
        return playback;
    }

    static GameObjectState1 parseGameObjectState(JSONObject gameObjectStateJson) {
        int start, end, color;
        Vec3 pos, rot, dim;
        TreeMap<String, String> sensors = new TreeMap<>();
        if (!gameObjectStateJson.isNull("sensors")) {
            Map<String, Object> sensObj = gameObjectStateJson.getJSONObject("sensors").toMap();
            for (Map.Entry<String, Object> entry : sensObj.entrySet()) {
                sensors.put(entry.getKey(), (String) entry.getValue());
            }
        }
        color = gameObjectStateJson.getInt("color");
        start = gameObjectStateJson.getInt("startingFrame");
        end = gameObjectStateJson.getInt("endingFrame");
        pos = parseVec3(gameObjectStateJson.getJSONObject("position"));
        rot = parseVec3(gameObjectStateJson.getJSONObject("rotation"));
        dim = parseVec3(gameObjectStateJson.getJSONObject("dimension"));

        return new GameObjectState1(color, start, end, pos, rot, dim, sensors);
    }

    static Vec3 parseVec3(JSONObject vec3Json) {
        float x, y, z;
        x = vec3Json.getFloat("x");
        y = vec3Json.getFloat("y");
        z = vec3Json.getFloat("z");
        return new Vec3(x, y, z);
    }

    // TODO different states
    static int parseStatus(String jsonStr) {
        JSONObject jsObj = new JSONObject(jsonStr);
        try {
            String status = jsObj.getString("status");
            if (!status.equals("online")) return -1;
            return jsObj.getInt("sim_count");
        } catch (Exception e) {
            System.err.println(e.toString());
            return -1;
        }
    }

    static boolean parseRequestStatus(String jsonStr) {
        JSONObject jsObj = new JSONObject(jsonStr);
        try {
            return jsObj.getBoolean("status");
        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
    }

    static CompileResult parseCompilationStatus(String jsonStr) {
        JSONObject jsObj = new JSONObject(jsonStr);
        try {
            boolean isCompiled = jsObj.getBoolean("status");
            String message = null;
            if (!isCompiled) {
                message = jsObj.getString("message");
            }
            String finalMessage = message;
            return new CompileResult() {
                @Override
                public boolean isCompiled() {
                    return isCompiled;
                }

                @Override
                public boolean isSimulated() {
                    return false;
                }

                @Override
                public String getMessage() {
                    return finalMessage;
                }
            };
        } catch (Exception e) {
            throw new ProcessingException(e.toString());
        }
    }
}
