package ru.nsu.fit.markelov.util;

import ru.nsu.fit.markelov.interfaces.client.Resource;

public class LevelResource implements Resource {

    private String name;
    private byte[] bytes;

    @Override
    public String getName() {
        return name;
    }

    public LevelResource setName(String name) {
        this.name = name;

        return this;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public LevelResource setBytes(byte[] bytes) {
        this.bytes = bytes;

        return this;
    }
}
