package simulator

interface SensorReadable {
    String getSensorReadings(int robotId, String sensor)

    String getGoal(int robotId)
}