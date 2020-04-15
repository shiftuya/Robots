package ru.nsu.fit.markelov.simulator;

import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.*;
import ru.nsu.fit.markelov.interfaces.server.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.CompileResult1;
import ru.nsu.fit.markelov.mainmanager.SimulationResultExtended;

import java.io.BufferedReader;
import java.io.IOException;
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
  public SimulationResultExtended runSimulation(
      String levelId, int lobbyId, Map<User, String> solutions) {
    ArrayList<Map.Entry<User, String>> entryList = new ArrayList<>(solutions.entrySet());
    ArrayList<String> sol = new ArrayList<>();
    for (Map.Entry<User, String> entry : entryList) {
      sol.add(entry.getValue());
    }
    String request = JsonUtil.formJSON(levelId, sol);
    try {
      URL url = monitor.chooseSim("/simulate");
      if (printDebug) {
        System.out.println(url.toString());
      }
      if (url == null) throw new MissingSimulationUnits("No Simulation Units available");
      String result = sendPOST(url, request.getBytes());
      return JsonUtil.parseSimResponse(lobbyId, result, entryList);
    } catch (Exception e) {
      throw new MissingSimulationUnits("No Simulation Units available");
    }
  }

  @Override
  public void addLevel(String name, String language, String levelSrc, List<Resource> resources) {
    StringBuilder errorMessage = new StringBuilder();
    boolean failed = false;
    synchronized (urls) {
      for (String urlStr : urls) {
        try {
          String res = sendPOST(urlStr + "/addLevel/" + name, levelSrc.getBytes());
          boolean uploaded = JsonUtil.parseRequestStatus(res);
          errorMessage.append(urlStr).append(" adding level: ");
          if (uploaded) {
            errorMessage.append("SUCCESS\n");
          } else {
            errorMessage.append("FAIL\n");
            failed = true;
          }
          if (resources != null) {
            for (Resource resource : resources) {
              res =
                  sendPOST(
                      urlStr + "/addResource/" + name + "/" + resource.getName(),
                      resource.getBytes());
              uploaded = JsonUtil.parseRequestStatus(res);
              errorMessage
                  .append(urlStr)
                  .append(" adding ")
                  .append(resource.getName())
                  .append(": ");
              if (uploaded) {
                errorMessage.append("SUCCESS\n");
              } else {
                errorMessage.append("FAIL\n");
                failed = true;
              }
            }
          }
        } catch (Exception e) {
          System.err.println(e.toString());
          failed = true;
          errorMessage.append(urlStr).append(": EXCEPTION. ").append(e.toString()).append("\n");
        }
      }
    }
    if (failed) {
      throw new ProcessingException(errorMessage.toString());
    }
  }

  @Override
  public void removeLevel(String name, String language) {
    StringBuilder errorMessage = new StringBuilder();
    boolean failed = false;
    synchronized (urls) {
      for (String url : urls) {
        try {
          String res = sendPOST(url + "/removeLevel/" + name, new byte[0]);
          boolean status = JsonUtil.parseRequestStatus(res);
          errorMessage.append(url).append(": ");
          if (status) {
            errorMessage.append("SUCCESS\n");
          } else {
            errorMessage.append("FAIL\n");
            failed = true;
          }
        } catch (Exception e) {
          failed = true;
          errorMessage.append(url).append(": ").append(e.toString()).append("\n");
        }
      }
    }
    if (failed) {
      throw new ProcessingException(errorMessage.toString());
    }
  }

  @Override
  public void updateLevel(String name, String language, String levelSrc, List<Resource> resources) {
    removeLevel(name, language);
    addLevel(name, language, levelSrc, resources);
  }

  @Override
  public CompileResult checkCompilation(String language, String solutionSrc) {
    URL url = monitor.chooseSim("/checkCompilation");
    try {
      String res = sendPOST(url, solutionSrc.getBytes());
      return JsonUtil.parseCompilationStatus(res);
    } catch (Exception e) {
      throw new ProcessingException(e.toString());
    }
  }

  private String sendPOST(String urlStr, byte[] data) throws IOException {
    return sendPOST(new URL(urlStr), data);
  }

  private String sendPOST(URL url, byte[] data) throws IOException {
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection) con;
    http.setRequestMethod("POST");
    http.setDoOutput(true);
    int length = data.length;
    http.setFixedLengthStreamingMode(length);
    http.connect();
    try (OutputStream os = http.getOutputStream()) {
      os.write(data);
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
    return json_response.toString();
  }
}
