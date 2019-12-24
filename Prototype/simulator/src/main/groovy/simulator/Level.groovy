package simulator

interface Level extends SensorReadable {
    int getPlayerCount()

    int setAction(int robotId, String action, double time)

    String getSensorReadings(int robotId, String sensor)

    String getGoal(int robotId)

    boolean checkGoal(int robotId)

    int getNextRobotId()

    boolean simulateUntilRFT(int robotId)

    double getVirtualTime()

    void breakRobot(int robotId)
}