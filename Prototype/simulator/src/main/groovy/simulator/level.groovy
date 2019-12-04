package simulator

interface level {
    int getPlayerCount()
    int performAction(int robotId, String action)
    String getSensorReadings(int robotId, String sensor)
    String getGoal(int robotId)
    boolean checkGoal(int robotId)
    int getNextRobotId()
}