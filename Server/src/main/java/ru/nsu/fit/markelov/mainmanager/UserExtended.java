package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.User;

public interface UserExtended extends User {
  String getPassword();
  void setPassword(String password);
  void setType(UserType type);
  void setAvatarAddress(String avatarAddress);
  void setBlocked(boolean blocked);
  void refreshLastActive();
}
