package ru.nsu.fit.markelov.simulator;

import org.json.JSONObject;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.interfaces.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.SimulationResult1;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HardcodedSimulatorManager implements SimulatorManager {
  private ArrayList<String> urls;

  public HardcodedSimulatorManager() {
    urls = new ArrayList<>();
    urls.add("http://localhost:1337");
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

  @Override
  public SimulationResult runSimulation( String levelId, int lobbyId, Map<Player, String> solutions) {
    ArrayList<Map.Entry<Player, String>> entryList = new ArrayList<>(solutions.entrySet());
    ArrayList<String> sol = new ArrayList<>();
    for (Map.Entry<Player, String> entry : entryList) {
      sol.add(entry.getValue());
    }
    String request = formJSON(levelId, sol);
    System.out.println(request);
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
      System.out.println(http.getResponseMessage());
      Map<String, Boolean> results = new HashMap<>();
    } catch (Exception e) {

    }
    /*def post = new URL("http://localhost:1337/simulate").openConnection();
    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    post.getOutputStream().write(request.getBytes("UTF-8"));
    def postRC = post.getResponseCode();
    String response = post.getInputStream().getText();
    println(response)
    def jsonSlurper = new JsonSlurper()
    def respObj = jsonSlurper.parseText(response)

    Map<Player, SimulationResult> results = new HashMap<>()
    Date now = new Date()
    if (respObj.timeout || respObj.broken) {
        for (def entry : entryList) {
            results.put(entry.key, new SimResultPlaceholder(now, false))
        }
    } else {
        def passed = respObj.results
        for (int i = 0; i < entryList.size(); i++) {
            results.put(entryList.get(i).key, new SimResultPlaceholder(now, passed.get(i)))
        }
    }*/
    return new SimulationResult1(0, null, null);
  }
}
