package ru.nsu.fit.markelov.interfaces.client;

public interface Resource {

    /**
     * Returns the resource name.
     *
     * @return the resource name.
     */
    String getName();

    /**
     * Returns the resource binary data.
     *
     * @return the resource binary data.
     */
    byte[] getBytes();
}
