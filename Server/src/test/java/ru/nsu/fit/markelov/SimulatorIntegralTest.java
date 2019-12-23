package ru.nsu.fit.markelov;

import org.junit.Before;
import org.junit.Test;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.simulator.HardcodedSimulatorManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SimulatorIntegralTest {
  private class PlayerTest implements Player {
    private String name;

    PlayerTest(String _name) {
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
    public boolean isSubmitted() {
      return false;
    }
  }

  String correctSolution;
  String wrongSolution;
  boolean inited;

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
      Player p1 = new PlayerTest("Nice Guy");
      Player p2 = new PlayerTest("Good Guy");
      HashMap<Player, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, correctSolution);
      HardcodedSimulatorManager hsm = new HardcodedSimulatorManager(true);
      SimulationResult result = hsm.runSimulation("simple_plane", 0, argMap);
      assertEquals(0, result.getId());
      assertTrue(result.isSuccessful(p1.getName()));
      assertTrue(result.isSuccessful(p2.getName()));
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }

  @Test
  public void testDifferentSolutions() {
    if (hostAvailabilityCheck()) {
      Player p1 = new PlayerTest("Good Guy");
      Player p2 = new PlayerTest("Bad Guy");

      HashMap<Player, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, wrongSolution);
      HardcodedSimulatorManager hsm = new HardcodedSimulatorManager(true);
      SimulationResult result = hsm.runSimulation("simple_plane", 0, argMap);
      System.out.println(p1.getName()+": "+result.isSuccessful(p1.getName()));
      System.out.println(p2.getName()+": "+result.isSuccessful(p2.getName()));
      assertEquals(0, result.getId());
      assertEquals(p1.getName()+" was wrong!",true, result.isSuccessful(p1.getName()));
      assertEquals(p2.getName()+" was correct!",false, result.isSuccessful(p2.getName()));
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }
}
