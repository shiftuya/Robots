package ru.nsu.fit.markelov.interfaces.client;

public interface Pair<K, V> {
    /**
     * Returns key of this pair.
     *
     * @return key of this pair.
     */
    K getKey();

    /**
     * Returns value of this pair.
     *
     * @return value of this pair.
     */
    V getValue();
}
