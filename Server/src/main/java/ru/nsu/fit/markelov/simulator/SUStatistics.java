package ru.nsu.fit.markelov.simulator;

import java.util.Date;

public class SUStatistics implements Comparable<SUStatistics> {
  private String url;
  private boolean online;
  private Date lastOnline;
  private Date lastUpdate;
  private int tasksRunning;

  public SUStatistics(String url, Date lastOnline, Date lastUpdate, int tasksRunning) {
    this.url = url;
    this.lastOnline = lastOnline;
    this.lastUpdate = lastUpdate;
    this.tasksRunning = tasksRunning;
    online = !lastOnline.before(lastUpdate);
  }

  public Date getLastOnline() {
    return lastOnline;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public int getTasksRunning() {
    return tasksRunning;
  }

  public void updateOnline(Date lastUpdate, int tasksRunning) {
    this.lastUpdate = lastUpdate;
    this.lastOnline = lastUpdate;
    this.tasksRunning = tasksRunning;
    online = true;
  }

  public void updateOffline(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    this.tasksRunning = 0;
    online = false;
  }

  public String getUrl() {
    return url;
  }

  public void incTask() {
    tasksRunning++;
  }

  public boolean isOnline() {
    return online;
  }

  @Override
  public int compareTo(SUStatistics suStatistics) {
    if (!online) {
      return 1;
    } else if (!suStatistics.online) {
      return -1;
    }
    return tasksRunning - suStatistics.tasksRunning;
  }
}
