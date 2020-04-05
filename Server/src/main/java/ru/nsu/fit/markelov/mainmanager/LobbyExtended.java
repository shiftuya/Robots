package ru.nsu.fit.markelov.mainmanager;

import java.util.Date;
import java.util.List;
import ru.nsu.fit.markelov.interfaces.client.Lobby;

public interface LobbyExtended extends Lobby {
  Date getCreationDate();
  List<UserExtended> getUsersWithoutPair();
  boolean isReady();
  void submit(UserExtended user, String code);
  void addPlayer(UserExtended player);
  void removePlayer(UserExtended player);
  void setReady(UserExtended player, boolean ready);
  String getCode(UserExtended user);
}
