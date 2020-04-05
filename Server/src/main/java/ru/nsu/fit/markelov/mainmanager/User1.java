package ru.nsu.fit.markelov.mainmanager;

import java.util.Date;

public class User1 implements UserExtended {
  private String avatarAddress;
  private String name;
  private UserType type;
  private boolean blocked;
  private Date lastActive;
  private String password;


  public User1(String avatarAddress, String name, UserType type, String password) {
    this.avatarAddress = avatarAddress;
    this.name = name;
    this.type = type;
    this.password = password;
    blocked = false;
    lastActive = new Date();
  }

  public User1(String avatarAddress, String name,
      UserType type, boolean blocked, Date lastActive, String password) {
    this.avatarAddress = avatarAddress;
    this.name = name;
    this.type = type;
    this.blocked = blocked;
    this.lastActive = lastActive;
    this.password = password;
  }

  @Override
  public String getAvatarAddress() {
    return avatarAddress;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public UserType getType() {
    return type;
  }

  @Override
  public Date getLastActive() {
    return lastActive;
  }

  @Override
  public boolean isBlocked() {
    return blocked;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public void setType(UserType type) {
    this.type = type;
  }

  @Override
  public void setAvatarAddress(String avatarAddress) {
    this.avatarAddress = avatarAddress;
  }

  @Override
  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  @Override
  public void refreshLastActive() {
    lastActive = new Date();
  }
}
