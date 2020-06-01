package simulator.playback

class GameObjectStateProto {
    int startingFrame, endingFrame
    Vec3Proto position, rotation, dimension
    int color
    Map<String, String> sensors

    GameObjectStateProto(int startingFrame, int color, Vec3Proto position, Vec3Proto rotation, Vec3Proto dimension) {
        this.startingFrame = startingFrame
        this.position = position
        this.rotation = rotation
        this.dimension = dimension
        this.color = color
        this.sensors = null
    }

    void addSensorReading(String sensor, String value) {
        if (sensors == null) {
            sensors = new HashMap<>()
        }
        sensors.put(sensor, value)
    }

    void setOldSensors(Map<String, String> sensors) {
        if (sensors == null) return
        if (this.sensors == null) {
            this.sensors = new HashMap<>()
        }
        for (def entry : sensors.entrySet()) {
            if (entry.key.endsWith("(Old)")) {
                this.sensors.put(entry.key, entry.value)
            } else this.sensors.put(entry.key + "(Old)", entry.value)
        }
    }
}
