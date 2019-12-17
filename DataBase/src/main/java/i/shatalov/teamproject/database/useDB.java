package i.shatalov.teamproject.database;

public class useDB {
    public static void main(String[]args) {
        updateDB changeDB = new updateDB();
        changeDB.insertNewRecords(100,
                "s.morozov",
                "morozz",
                "Student");
        System.out.println(changeDB.takeRecord(100,"LoginName"));
        changeDB.updateRecord(100, "LoginName", "Morozzz.S");
        System.out.println(changeDB.takeRecord(100,"LoginName"));
    }
}
