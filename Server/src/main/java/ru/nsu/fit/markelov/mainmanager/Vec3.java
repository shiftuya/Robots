package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.client.playback.Vector3;

public class Vec3 implements Vector3 {
    private float x,y,z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
