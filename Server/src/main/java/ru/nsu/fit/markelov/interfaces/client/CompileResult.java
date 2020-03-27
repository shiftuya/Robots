package ru.nsu.fit.markelov.interfaces.client;

public interface CompileResult {
    /**
     * Returns whether the compilation has been successful.
     *
     * @return whether the compilation has been successful.
     */
    boolean isCompiled();
    /**
     * Returns whether the simulation has been processed.
     *
     * @return whether the simulation has been processed.
     */
    boolean isSimulated();

    /**
     * Returns the compile message.
     *
     * @return the compile message.
     */
    String getMessage();
}
