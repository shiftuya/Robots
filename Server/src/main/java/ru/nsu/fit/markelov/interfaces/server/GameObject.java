package ru.nsu.fit.markelov.interfaces.server;

public interface GameObject {

    /**
     * Returns unique id of this object.
     *
     * @return unique id of this object.
     */
    int getId();

    /**
     * Returns the dimension of this object.
     *
     * @return the dimension of this object.
     */
    Vector3 getDimension();
}
