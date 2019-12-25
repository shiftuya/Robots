package i.shatalov.teamproject.database;

public class useDB {
    public static void main(String[]args) {
        updateDB changeDB = new updateDB();
        changeDB.insertNewRecords(102,
                "spykex3",
                "robot",
                "Student");
        System.out.println(changeDB.takeRecord(100,"LoginName"));
        changeDB.updateRecord(100, "LoginName", "S.Morozzz");
        System.out.println(changeDB.takeRecord(100,"LoginName"));
        System.out.println(changeDB.takeRecord(102, "LoginName"));
        System.out.println(changeDB.takeRecord(102, "Password"));
        System.out.println(changeDB.takeRecord(102, "AccountType"));
        changeDB.deleteUser(102);
    }
}
