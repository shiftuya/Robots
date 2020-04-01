package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.Player;

class Player1 implements Player {
  private String avatarAddress;
  private String name;
  private String solutionCode;

  private boolean submitted;

  void setSolutionCode(String code) {
    solutionCode = code;
  }

  void setSubmitted(boolean submitted) {
    this.submitted = submitted;
  }

  Player1(String avatarAddress, String name) {
    this.avatarAddress = avatarAddress;
    this.name = name;
    submitted = false;
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
  public boolean isSubmitted() {
    return submitted;
  }
}
