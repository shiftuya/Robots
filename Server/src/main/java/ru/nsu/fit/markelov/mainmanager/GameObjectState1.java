package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.playback.GameObjectState;
import ru.nsu.fit.markelov.interfaces.client.playback.Vector3;

import java.util.Map;
import java.util.TreeMap;

public class GameObjectState1 implements GameObjectState {

    private int color, startingFrame, endingFrame;
    private Vector3 position, rotation, dimension;
    private Map<String, String> sensors;

    public GameObjectState1(
            int color,
            int startingFrame,
            int endingFrame,
            Vector3 position,
            Vector3 rotation,
            Vector3 dimension) {
        this.color = color;
        this.startingFrame = startingFrame;
        this.endingFrame = endingFrame;
        this.position = position;
        this.rotation = rotation;
        this.dimension = dimension;
        this.sensors = new TreeMap<>();
    }

    public GameObjectState1(
            int color,
            int startingFrame,
            int endingFrame,
            Vector3 position,
            Vector3 rotation,
            Vector3 dimension,
            Map<String, String> sensors) {
        this.color = color;
        this.startingFrame = startingFrame;
        this.endingFrame = endingFrame;
        this.position = position;
        this.rotation = rotation;
        this.dimension = dimension;
        this.sensors = sensors;
    }

    @Override
    public int getStartingFrame() {
        return startingFrame;
    }

    @Override
    public int getEndingFrame() {
        return endingFrame;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public Vector3 getDimension() {
        return dimension;
    }

    @Override
    public Vector3 getRotation() {
        return rotation;
    }

    @Override
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setStartingFrame(int startingFrame) {
        this.startingFrame = startingFrame;
    }

    public void setEndingFrame(int endingFrame) {
        this.endingFrame = endingFrame;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public void setDimension(Vector3 dimension) {
        this.dimension = dimension;
    }

    public Map<String, String> getSensorValues() {
        return sensors;
    }

    public void setSensorValues(Map<String, String> sensors) {
        this.sensors = sensors;
    }
}
