package simulator

interface SensorReadable {
    String getSensorReadings(String sensor)

    double getVirtualTime()

    void writeLog(String str)

    String getGoal()
}