package ru.nsu.fit.markelov.objects;

import ru.nsu.fit.markelov.interfaces.CompileResult;

public class CompileResultClass implements CompileResult {

    private boolean compiled;
    private String message;

    @Override
    public boolean isCompiled() {
        return compiled;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
