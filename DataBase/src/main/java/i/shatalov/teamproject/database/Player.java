package i.shatalov.teamproject.database;

public class Player {
  String LogName;
  String Password;
  String AccType;

  public Player(int id, String LogName, String Password, String AccType){
    this.LogName = LogName;
    this.Password = Password;
    this.AccType = AccType;
  }
}
