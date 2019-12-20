package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.Player;

class Player1 implements Player {
  private String avatarAddress;
  private String name;
  private String solutionCode;

  public Player1(String avatarAddress, String name) {
    this.avatarAddress = avatarAddress;
    this.name = name;
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
    return solutionCode != null;
  }
}
