package i.shatalov.teamproject.database;

public class PlayerClass implements Player {
  private String LogName;
  private String Password;
  private String AccType;

  public PlayerClass(String LogName, String Password, String AccType){
    this.LogName = LogName;
    this.Password = Password;
    this.AccType = AccType;
  }

  public String getName() {
    return this.LogName;
  }

  public String getPass() {
    return this.Password;
  }

  public String getAcc() {
    return this.AccType;
  }
}
