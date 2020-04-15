package ru.nsu.fit.markelov.simulator;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.mainmanager.SimulationResult1;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    } catch (Exception e) {
      for (int i = 0; i < entryList.size(); i++) {
        passed.put(entryList.get(i).getKey().getName(), false);
      }
    }
    return new SimulationResult1(id, passed, logs, new Date());
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
}
