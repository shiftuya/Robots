package simulator.playback

import groovy.transform.CompileStatic

import java.util.stream.Collectors

@CompileStatic
class PlaybackCreator {
    private HashMap<Integer, ArrayList<GameObjectStateProto>> entities
    int time

    public PlaybackCreator(int robots, int environment) {
        entities = new HashMap<>()
        for (int i = 0; i < robots; i++) {
            ArrayList<GameObjectStateProto> state = new ArrayList<GameObjectStateProto>()
            state.add(new GameObjectStateProto(0, 0, new Vec3Proto(0, 0, 0), new Vec3Proto(0, 0, 0), new Vec3Proto(0, 0, 0)))
            entities.put(i, state)

        }
        for (int i = robots; i < robots + environment; i++) {
            ArrayList<GameObjectStateProto> state = new ArrayList<GameObjectStateProto>()
            state.add(new GameObjectStateProto(0, 0, new Vec3Proto(0, 0, 0), new Vec3Proto(0, 0, 0), new Vec3Proto(0, 0, 0)))
            entities.put(i, state)
        }
    }

    public updateColor(int id, int newColor) {
        GameObjectStateProto prev = entities.get(id).last()
        if (prev.getStartingFrame() == time) {
            prev.color = newColor
        } else {
            prev.setEndingFrame(time)
            GameObjectStateProto current = new GameObjectStateProto(time, newColor, prev.getPosition(), prev.getRotation(), prev.getDimension())
            current.setOldSensors(prev.sensors)
            entities.get(id).add(current)
        }
    }

    public updatePosition(int id, Vec3Proto newPosition) {
        GameObjectStateProto prev = entities.get(id).last()
        if (prev.getStartingFrame() == time) {
            prev.position = newPosition
        } else {
            prev.setEndingFrame(time)
            GameObjectStateProto current = new GameObjectStateProto(time, prev.getColor(), newPosition, prev.getRotation(), prev.getDimension())
            current.setOldSensors(prev.sensors)
            entities.get(id).add(current)
        }
    }


    public updateRotation(int id, Vec3Proto newRotation) {
        GameObjectStateProto prev = entities.get(id).last()
        if (prev.getStartingFrame() == time) {
            prev.rotation = newRotation
        } else {
            prev.setEndingFrame(time)
            GameObjectStateProto current = new GameObjectStateProto(time, prev.getColor(), prev.getPosition(), newRotation, prev.getDimension())
            current.setOldSensors(prev.sensors)
            entities.get(id).add(current)
        }
    }

    public updateDimension(int id, Vec3Proto newDimension) {
        GameObjectStateProto prev = entities.get(id).last()
        if (prev.getStartingFrame() == time) {
            prev.dimension = newDimension
        } else {
            prev.setEndingFrame(time)
            GameObjectStateProto current = new GameObjectStateProto(time, prev.getColor(), prev.getPosition(), prev.getRotation(), newDimension)
            current.setOldSensors(prev.sensors)
            entities.get(id).add(current)
        }
    }

    public updateSensor(int id, String sensor, String value) {
        GameObjectStateProto prev = entities.get(id).last()
        if (prev.getStartingFrame() == time) {
            prev.addSensorReading(sensor, value)
        } else {
            prev.setEndingFrame(time)
            GameObjectStateProto current = new GameObjectStateProto(time, prev.getColor(), prev.getPosition(), prev.getRotation(), prev.getDimension())
            current.setOldSensors(prev.sensors)
            current.addSensorReading(sensor, value)
            entities.get(id).add(current)
        }
    }

    public breakpoint(int id) {
        GameObjectStateProto prev = entities.get(id).last()
        prev.setEndingFrame(time)
        GameObjectStateProto skip = new GameObjectStateProto(time, prev.getColor(), prev.getPosition(), prev.getRotation(), prev.getDimension())
        skip.endingFrame = time
        entities.get(id).add(skip)
        GameObjectStateProto current = new GameObjectStateProto(time, prev.getColor(), prev.getPosition(), prev.getRotation(), prev.getDimension())
        current.setOldSensors(prev.sensors)
        entities.get(id).add(current)
    }

    public Playback getPlayback() {
        List<List<GameObjectStateProto>> list = entities.entrySet().stream().sorted({ e1, e2 -> (e1.key <=> e2.key) }).map({ e -> e.value }).collect(Collectors.toList())
        return new PlaybackProto(time, list)
    }

}
