package ru.nsu.fit.markelov.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class SUMonitor extends Thread {
  private final List<SUStatistics> statistics;
  private boolean stop;

  public SUMonitor(List<String> _urls) {
    Date now = new Date();
    statistics = new ArrayList<>();
    for (String urlStr : _urls) {
      SUStatistics stat = new SUStatistics(urlStr, now, now, 0);
      stat.updateOffline(now);
      statistics.add(stat);
    }
  }

  public SUMonitor() {
    statistics = new ArrayList<>();
  }

  public void addSU(String url) {
    Date now = new Date();
    SUStatistics stat = new SUStatistics(url, now, now, 0);
    stat.updateOnline(now, 0);
    synchronized (statistics) {
      statistics.add(stat);
    }
  }

  public void removeSU(String url) {
    synchronized (statistics) {
      for (SUStatistics stat : statistics) {
        if (stat.getUrl().equals(url)) {
          statistics.remove(stat);
        }
      }
    }
  }

  public int checkSU(String urlStr) {
    try {
      URL url = new URL(urlStr + "/test");
      URLConnection con = url.openConnection();
      HttpURLConnection http = (HttpURLConnection) con;
      http.setConnectTimeout(3000);
      http.setRequestMethod("GET");
      http.connect();
      StringBuilder json_response = new StringBuilder();
      InputStreamReader in = new InputStreamReader(http.getInputStream());
      BufferedReader br = new BufferedReader(in);
      String text;
      while ((text = br.readLine()) != null) {
        json_response.append(text);
      }
      return JsonUtil.parseStatus(json_response.toString());
    } catch (IOException ignore) {
      return -1;
    }
  }

  private void statusUpdate(SUStatistics stat) {
    try {
      int tasks = checkSU(stat.getUrl());
      if (tasks == -1) {
        stat.updateOffline(new Date());
        return;
      }
      stat.updateOnline(new Date(), tasks);
    } catch (Exception e) {
      stat.updateOffline(new Date());
    }
  }

  URL chooseSim() {
    synchronized (statistics) {
      if (statistics.isEmpty()) {
        return null;
      }
      Collections.sort(statistics);
      SUStatistics best = statistics.get(0);
      if (!best.isOnline()) {
        return null;
      }
      URL result;
      try {
        result = new URL(best.getUrl() + "/simulate");
      } catch (MalformedURLException e) {
        return null;
      }
      best.incTask();
      return result;
    }
  }

  public void finish() {
    stop = true;
  }

  @Override
  public void run() {
    while (!stop) {
      for (int i = 0; i < statistics.size(); i++) {
        synchronized (statistics) {
          if (i > statistics.size()) break;
          try {
            SUStatistics current = statistics.get(i);
            statusUpdate(current);
          } catch (Exception e) {
            System.err.println(e.toString());
          }
        }
      }
      try {
        sleep(200);
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}
