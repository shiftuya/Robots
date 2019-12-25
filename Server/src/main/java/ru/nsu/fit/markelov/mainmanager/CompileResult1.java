package ru.nsu.fit.markelov.mainmanager;

import ru.nsu.fit.markelov.interfaces.CompileResult;

public class CompileResult1 implements CompileResult {
  private String message;
  private boolean compiled;
  private boolean simulated;

  CompileResult1(String message, boolean compiled, boolean simulated) {
    this.message = message;
    this.compiled = compiled;
    this.simulated = simulated;
  }

  @Override
  public boolean isCompiled() {
    return compiled;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isSimulated() {
    return simulated;
  }
}
