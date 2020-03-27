package ru.nsu.fit.markelov.simulator;

import ru.nsu.fit.markelov.interfaces.client.Level;
import ru.nsu.fit.markelov.interfaces.client.Player;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.server.SimulatorManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class HardcodedSimulatorManager implements SimulatorManager {
  private final List<String> urls;
  private boolean printDebug;
  private SUMonitor monitor;

  public HardcodedSimulatorManager() {
    urls = Collections.synchronizedList(new ArrayList<>());
    monitor = new SUMonitor();
    monitor.start();
  }

  public HardcodedSimulatorManager(boolean printLog) {
    urls = Collections.synchronizedList(new ArrayList<>());
    printDebug = printLog;
    monitor = new SUMonitor();
    monitor.start();
  }

  public HardcodedSimulatorManager(List<String> urls) {
    this.urls = new ArrayList<>();
    this.urls.addAll(urls);
    monitor = new SUMonitor(this.urls);
    monitor.start();
  }

  @Override
  public boolean addSimulator(String url) {
    synchronized (urls) {
      if (urls.contains(url)) return false;
      monitor.addSU(url);
      return urls.add(url);
    }
  }

  @Override
  public boolean removeSimulator(String url) {
    synchronized (urls) {
      monitor.removeSU(url);
      return urls.remove(url);
    }
  }

  @Override
  public List<String> getSimulatorsList() {
    return urls;
  }

  @Override
  public SimulationResult runSimulation(
      String levelId, int lobbyId, Map<Player, String> solutions) {
    ArrayList<Map.Entry<Player, String>> entryList = new ArrayList<>(solutions.entrySet());
    ArrayList<String> sol = new ArrayList<>();
    for (Map.Entry<Player, String> entry : entryList) {
      sol.add(entry.getValue());
    }
    String request = JsonUtil.formJSON(levelId, sol);
    try {
      URL url = monitor.chooseSim();
      if(printDebug){
        System.out.println(url.toString());
      }
      if (url == null) throw new MissingSimulationUnits("No Simulation Units available");

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
      if (printDebug) {
        System.out.println(json_response.toString());
      }
      return JsonUtil.parseSimResponse(lobbyId, json_response.toString(), entryList);
    } catch (Exception e) {
      throw new MissingSimulationUnits("No Simulation Units available");
    }
  }

  @Override
  public List<Level> getLevels() {
    return null;
  }

  @Override
  public boolean addLevel(String name, String source, String language) {
    return false;
  }

  @Override
  public boolean removeLevel(String name, String language) {
    return false;
  }

  @Override
  public boolean updateLevel(String name, String source, String language) {
    return false;
  }
}
