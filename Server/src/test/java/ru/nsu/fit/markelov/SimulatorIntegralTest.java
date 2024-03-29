package ru.nsu.fit.markelov;

import org.junit.Before;
import org.junit.Test;
import ru.nsu.fit.markelov.interfaces.ProcessingException;
import ru.nsu.fit.markelov.interfaces.client.CompileResult;
import ru.nsu.fit.markelov.interfaces.client.User;
import ru.nsu.fit.markelov.interfaces.client.SimulationResult;
import ru.nsu.fit.markelov.interfaces.server.SimulatorManager;
import ru.nsu.fit.markelov.mainmanager.SimulationResultExtended;
import ru.nsu.fit.markelov.simulator.HardcodedSimulatorManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SimulatorIntegralTest {
  private class UserTest implements User {
    private String name;

    UserTest(String _name) {
      name = _name;
    }

    @Override
    public String getAvatarAddress() {
      return null;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public UserType getType() {
      return null;
    }

    @Override
    public Date getLastActive() {
      return null;
    }

    @Override
    public boolean isBlocked() {
      return false;
    }
  }

  private class CorrectSim implements Runnable {

    SimulatorManager sm;

    CorrectSim(SimulatorManager sm) {
      this.sm = sm;
    }

    @Override
    public void run() {
      try {
        User p1 = new UserTest("Good Guy");
        User p2 = new UserTest("Bad Guy");
        HashMap<User, String> argMap = new HashMap<>();
        argMap.put(p1, correctSolution);
        argMap.put(p2, wrongSolution);
        SimulationResultExtended result = sm.runSimulation("spl", 0, argMap);
        System.out.println(p1.getName() + ": " + result.isSuccessful(p1.getName()));
        System.out.println(p2.getName() + ": " + result.isSuccessful(p2.getName()));
        assertEquals(0, result.getId());
        if (!result.isSuccessful(p1.getName())) {
          System.err.println(result.getLog(p1.getName()));
        }
        assertTrue(p1.getName() + " was wrong!", result.isSuccessful(p1.getName()));
        assertFalse(p2.getName() + " was correct!", result.isSuccessful(p2.getName()));
      } catch (Exception e) {
        System.err.println(e.toString());
        fail();
      }
    }
  }

  private String correctSolution;
  private String wrongSolution;
  private String levelSrc;
  private SimulatorManager hsm;
  private boolean inited;

  private String readFile(String filePath) {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        contentBuilder.append(sCurrentLine).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return contentBuilder.toString();
  }

  @Before
  public void init() {
    if (!inited) {
      correctSolution = readFile("src/test/resources/solution_spl.groovy");
      wrongSolution = readFile("src/test/resources/wrong_solution_spl.groovy");
      levelSrc = readFile("src/test/resources/simple_plane_lvl.groovy");
      hsm = new HardcodedSimulatorManager(true);
      hsm.addSimulator("http://localhost:1337");
      try {
        hsm.removeLevel("spl", "groovy");
      } catch (ProcessingException ignore) {
      }
      System.out.println("Adding level");
      hsm.addLevel("spl", "groovy", levelSrc, null);
      inited = true;
    }
  }

  private static boolean hostAvailabilityCheck() {
    try (Socket s = new Socket("localhost", 1337)) {
      return true;
    } catch (IOException ex) {
      /* ignore */
    }
    return false;
  }

  @Test
  public void testCorrectSolutions() {
    if (hostAvailabilityCheck()) {
      User p1 = new UserTest("Nice Guy");
      User p2 = new UserTest("Good Guy");
      HashMap<User, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, correctSolution);
      SimulationResult result = hsm.runSimulation("spl", 0, argMap);
      assertEquals(0, result.getId());
      assertTrue(result.isSuccessful(p1.getName()));
      assertTrue(result.isSuccessful(p2.getName()));
      System.out.println("Simulated");
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }

  @Test
  public void testDifferentSolutions() {
    if (hostAvailabilityCheck()) {
      User p1 = new UserTest("Good Guy");
      User p2 = new UserTest("Bad Guy");

      HashMap<User, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, wrongSolution);
      SimulationResult result = hsm.runSimulation("spl", 0, argMap);
      System.out.println(p1.getName() + ": " + result.isSuccessful(p1.getName()));
      System.out.println(p2.getName() + ": " + result.isSuccessful(p2.getName()));
      assertEquals(0, result.getId());
      assertEquals(p1.getName() + " was wrong!", true, result.isSuccessful(p1.getName()));
      assertEquals(p2.getName() + " was correct!", false, result.isSuccessful(p2.getName()));
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }

  @Test
  public void loadTest() {
    if (hostAvailabilityCheck()) {
      User p1 = new UserTest("Good Guy");
      User p2 = new UserTest("Bad Guy");

      HashMap<User, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, wrongSolution);
      try {
        hsm.addSimulator("http://192.168.0.104:1337");
      } catch (ProcessingException ignore) {

      }
      ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
      for (int i = 0; i < 50; i++) {
        executor.execute(new CorrectSim(hsm));
      }
      executor.shutdown();
      try {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      } catch (InterruptedException e) {
        System.err.println(e.toString());
        fail();
      }
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
      System.out.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }

  @Test
  public void testRemove() {
    if (hostAvailabilityCheck()) {
      User p1 = new UserTest("Good Guy");
      User p2 = new UserTest("Bad Guy");

      HashMap<User, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, wrongSolution);
      hsm.addLevel("spl1", "groovy", levelSrc, null);
      SimulationResult result = hsm.runSimulation("spl1", 0, argMap);
      hsm.removeLevel("spl1", "groovy");
      System.out.println(p1.getName() + ": " + result.isSuccessful(p1.getName()));
      System.out.println(p2.getName() + ": " + result.isSuccessful(p2.getName()));
      assertEquals(0, result.getId());
      assertTrue(p1.getName() + " was wrong!", result.isSuccessful(p1.getName()));
      assertFalse(p2.getName() + " was correct!", result.isSuccessful(p2.getName()));
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }

  @Test
  public void testCompilation() {
    if (hostAvailabilityCheck()) {
      CompileResult cr1 = hsm.checkCompilation("groovy", correctSolution);
      CompileResult cr2 = hsm.checkCompilation("groovy", wrongSolution);
      CompileResult cr3 =
          hsm.checkCompilation("groovy", "Hello I am not code but 4ye 4|V| 4 1337 H4x0r");
      assertTrue(cr1.isCompiled());
      assertTrue(cr2.isCompiled());
      assertFalse(cr3.isCompiled());
    }
  }
}
