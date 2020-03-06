package simulator

interface Level{
    int getPlayerCount()

    int setAction(int robotId, String action, double time)

    String getSensorReadings(int robotId, String sensor)

    String getGoal(int robotId)

    boolean checkGoal(int robotId)

    int getNextRobotId()

    boolean simulateUntilRFT(int robotId)

    double getVirtualTime()

    void breakRobot(int robotId)

    void writeLog(int robotId, String str)

    String getLog(int robotId)
}