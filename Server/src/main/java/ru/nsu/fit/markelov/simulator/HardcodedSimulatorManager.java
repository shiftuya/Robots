package ru.nsu.fit.markelov.simulator;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.SimulationResult1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class HardcodedSimulatorManager implements SimulatorManager {
  private ArrayList<String> urls;
  private boolean printDebug;

  public HardcodedSimulatorManager() {
    urls = new ArrayList<>();
    urls.add("http://localhost:1337/simulate");
  }

  public HardcodedSimulatorManager(boolean printLog) {
    urls = new ArrayList<>();
    urls.add("http://localhost:1337/simulate");
    printDebug = printLog;
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
      if (jsObj.getBoolean("timeout") || jsObj.getBoolean("broken")) {
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
    if(printDebug){
      for (Map.Entry<Player, String>entry : entryList) {
        System.out.println("\n\n"+entry.getKey().getName()+"\t"+entry.getValue()+"\n\n");
      }
    }
    ArrayList<String> sol = new ArrayList<>();
    for (Map.Entry<Player, String> entry : entryList) {
      sol.add(entry.getValue());
    }
    //Collections.reverse(sol);
    String request = formJSON(levelId, sol);
    if(printDebug){
      System.out.println(request);
    }
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
      String text;
      while ((text = br.readLine()) != null) {
        json_response.append(text);
      }
      if(printDebug){
        System.out.println(json_response.toString());
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
