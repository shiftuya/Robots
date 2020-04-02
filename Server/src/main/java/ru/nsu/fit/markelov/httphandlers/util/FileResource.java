package ru.nsu.fit.markelov.httphandlers.util;

import ru.nsu.fit.markelov.interfaces.client.Resource;

public class FileResource implements Resource {

    private String name;
    private byte[] bytes;

    @Override
    public String getName() {
        return name;
    }

    public FileResource setName(String name) {
        this.name = name;

        return this;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public FileResource setBytes(byte[] bytes) {
        this.bytes = bytes;

        return this;
    }
}
