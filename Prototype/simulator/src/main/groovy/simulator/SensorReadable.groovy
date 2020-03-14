package simulator

interface SensorReadable {
    Object getSensorReadings(String sensor)

    double getVirtualTime()

    void writeLog(String str)

    String getGoal()
}