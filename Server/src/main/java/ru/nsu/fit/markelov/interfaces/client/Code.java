package ru.nsu.fit.markelov.interfaces.client;

public interface Code {
    /**
     * Returns the language of the source code.
     *
     * @return the language of the source code.
     */
    String getLanguage();

    /**
     * Returns the source code.
     *
     * @return the source code.
     */
    String getCode();
}
