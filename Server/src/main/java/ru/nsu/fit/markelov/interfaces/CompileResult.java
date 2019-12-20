package ru.nsu.fit.markelov.interfaces;

//import com.sun.istack.internal.NotNull;

public interface CompileResult {
    /**
     * Returns whether the compilation has been successful.
     *
     * @return whether the compilation has been successful.
     */
    boolean isCompiled();

    /**
     * Returns the compile message.
     *
     * @return the compile message.
     */
    //@NotNull
    String getMessage();
}
