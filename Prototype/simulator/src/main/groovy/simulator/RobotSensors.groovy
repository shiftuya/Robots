package simulator

class RobotSensors implements SensorReadable {
    private Level lvl
    private int robotId

    RobotSensors(Level _lvl, int _robotId) {
        lvl = _lvl
        robotId = _robotId
    }

    @Override
    String getSensorReadings(String sensor) {
        return lvl.getSensorReadings(robotId, sensor)
    }

    @Override
    double getVirtualTime() {
        return lvl.getVirtualTime()
    }

    @Override
    void writeLog(String str) {
        lvl.writeLog(robotId, str)
    }

    @Override
    String getGoal() {
        return lvl.getGoal(robotId)
    }
}
