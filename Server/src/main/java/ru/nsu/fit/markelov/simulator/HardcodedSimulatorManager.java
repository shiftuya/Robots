package ru.nsu.fit.markelov.simulator;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.SimulationResult1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HardcodedSimulatorManager implements SimulatorManager {
  private ArrayList<String> urls;

  public HardcodedSimulatorManager() {
    urls = new ArrayList<>();
    urls.add("http://localhost:1337/simulate");
  }

  @Override
  public boolean addSimulator(String url) {
    return false;
  }

  @Override
  public boolean removeSimulator(String url) {
    return false;
  }

  @Override
  public ArrayList<String> getSimulatorsList() {
    return urls;
  }

  private String formJSON(String levelId, ArrayList<String> solutionList) {
    JSONObject jsObj = new JSONObject();
    jsObj.put("level", levelId);
    jsObj.put("solutions", solutionList);
    return jsObj.toString();
  }

  private ArrayList<Boolean> parseResponse(String jsonStr, int playerCount) {
    JSONObject jsObj = new JSONObject(jsonStr);
    ArrayList<Boolean> results = new ArrayList<>();
    try {
      if ((Boolean) jsObj.get("timeout") || (Boolean) jsObj.get("broken")) {
        for (int i = 0; i < playerCount; i++) {
          results.add(false);
        }
      } else {
        JSONArray arr = jsObj.getJSONArray("results");
        for (int i = 0; i < arr.length(); i++) {
          results.add(arr.getBoolean(i));
        }
      }
    } catch (Exception e) {
      for (int i = 0; i < playerCount; i++) {
        results.add(false);
      }
    }
    return results;
  }

  @Override
  public SimulationResult runSimulation(
      String levelId, int lobbyId, Map<Player, String> solutions) {
    ArrayList<Map.Entry<Player, String>> entryList = new ArrayList<>(solutions.entrySet());
    ArrayList<String> sol = new ArrayList<>();
    for (Map.Entry<Player, String> entry : entryList) {
      sol.add(entry.getValue());
    }
    String request = formJSON(levelId, sol);
    try {
      URL url = new URL(urls.get(0));
      URLConnection con = url.openConnection();
      HttpURLConnection http = (HttpURLConnection) con;
      http.setRequestMethod("POST");
      http.setDoOutput(true);
      byte[] out = request.getBytes();
      int length = out.length;
      http.setFixedLengthStreamingMode(length);
      http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      http.connect();
      try (OutputStream os = http.getOutputStream()) {
        os.write(out);
      }
      StringBuilder json_response = new StringBuilder();
      InputStreamReader in = new InputStreamReader(http.getInputStream());
      BufferedReader br = new BufferedReader(in);
      String text = "";
      while ((text = br.readLine()) != null) {
        json_response.append(text);
      }
      ArrayList<Boolean> respRes = parseResponse(json_response.toString(), entryList.size());
      HashMap<String, Boolean> results = new HashMap<>();
      for (int i = 0; i < respRes.size(); i++) {
        results.put(entryList.get(i).getKey().getName(), respRes.get(i));
      }
      return new SimulationResult1(lobbyId, results, new Date());
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return new SimulationResult1(-1, null, null);
    }
  }
}
