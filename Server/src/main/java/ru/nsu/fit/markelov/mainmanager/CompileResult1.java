package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.CompileResult;

public class CompileResult1 implements CompileResult {
  private String message;
  private boolean compiled;

  public CompileResult1(String message, boolean compiled) {
    this.message = message;
    this.compiled = compiled;
  }

  @Override
  public boolean isCompiled() {
    return compiled;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
