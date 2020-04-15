package ru.nsu.fit.markelov.simulator;

import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.*;
import ru.nsu.fit.markelov.interfaces.server.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.CompileResult1;

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
  public void addSimulator(String url) {
    synchronized (urls) {
      if (urls.contains(url)) throw new ProcessingException("Same simulator unit already exists.");
      if (monitor.checkSU(url) < 0) {
        throw new ProcessingException("Failed to connect to simulation unit.");
      }
      monitor.addSU(url);
      urls.add(url);
    }
  }

  @Override
  public void removeSimulator(String url) {
    synchronized (urls) {
      monitor.removeSU(url);
      urls.remove(url);
    }
  }

  @Override
  public List<String> getSimulatorsList() {
    return urls;
  }

  @Override
  public SimulationResult runSimulation(String levelId, int lobbyId, Map<User, String> solutions) {
    ArrayList<Map.Entry<User, String>> entryList = new ArrayList<>(solutions.entrySet());
    ArrayList<String> sol = new ArrayList<>();
    for (Map.Entry<User, String> entry : entryList) {
      sol.add(entry.getValue());
    }
    String request = JsonUtil.formJSON(levelId, sol);
    try {
      URL url = monitor.chooseSim();
      if (printDebug) {
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
  public void addLevel(String name, String language, String levelSrc, List<Resource> resources) {}

  @Override
  public void removeLevel(String name, String language) {}

  @Override
  public void updateLevel(
      String name, String language, String levelSrc, List<Resource> resources) {}

  @Override
  public CompileResult checkCompilation(String language, String solutionSrc) {
    return new CompileResult() {
      @Override
      public boolean isCompiled() {
        return true;
      }

      @Override
      public boolean isSimulated() {
        return false;
      }

      @Override
      public String getMessage() {
        return null;
      }
    };
  }
}
