package ru.nsu.fit.markelov;

import org.junit.Test;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.objects_hardcoded.PlayerHardcoded;
import ru.nsu.fit.markelov.simulator.HardcodedSimulatorManager;

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
      String correctSolution =
          String.join(
              "\n",
              "package simulator",
              "String goalStr = level.getGoal(robotId)",
              "def goals = goalStr.split()",
              "int gx = goals[0].toInteger()",
              "int gy = goals[1].toInteger()",
              "String xStr = level.getSensorReadings(robotId, \"x\")",
              "String yStr = level.getSensorReadings(robotId, \"y\")",
              "def y = yStr.toInteger()",
              "def x = xStr.toInteger()",
              "if (x < gx)",
              "    return \"right \" + (gx - x)",
              "if (x > gx)",
              "    return \"left \" + (x - gx)",
              "if (y < gy)",
              "    return \"up \" + (gy - y)",
              "if (y > gy)",
              "    return \"down \" + (y - gy)",
              "return \"stay 1\"");
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
      String correctSolution =
          String.join(
              "\n",
              "package simulator",
              "// CORRECT",
              "String goalStr = level.getGoal(robotId)",
              "def goals = goalStr.split()",
              "int gx = goals[0].toInteger()",
              "int gy = goals[1].toInteger()",
              "String xStr = level.getSensorReadings(robotId, \"x\")",
              "String yStr = level.getSensorReadings(robotId, \"y\")",
              "def y = yStr.toInteger()",
              "def x = xStr.toInteger()",
              "if (x < gx)",
              "    return \"right \" + (gx - x)",
              "if (x > gx)",
              "    return \"left \" + (x - gx)",
              "if (y < gy)",
              "    return \"up \" + (gy - y)",
              "if (y > gy)",
              "    return \"down \" + (y - gy)",
              "return \"stay 1\"");
      String wrongSolution =
          String.join(
              "\n",
              "package simulator",
              "//WRONG",
              "String goalStr = level.getGoal(robotId)",
              "def goals = goalStr.split()",
              "int gx = goals[0].toInteger()",
              "int gy = goals[1].toInteger()",
              "String xStr = level.getSensorReadings(robotId, \"x\")",
              "String yStr = level.getSensorReadings(robotId, \"y\")",
              "def y = yStr.toInteger()",
              "def x = xStr.toInteger()",
              "if (x < gx)",
              "    return \"left 150\"",
              "if (x > gx)",
              "    return \"right 150\"",
              "if (y < gy)",
              "    return \"down 150\"",
              "if (y > gy)",
              "    return \"up 150\"",
              "return \"stay 1\"");
      HashMap<Player, String> argMap = new HashMap<>();
      argMap.put(p1, correctSolution);
      argMap.put(p2, wrongSolution);
      HardcodedSimulatorManager hsm = new HardcodedSimulatorManager(true);
      SimulationResult result = hsm.runSimulation("simple_plane", 0, argMap);
      assertEquals(0, result.getId());
      assertEquals(true, result.isSuccessful(p1.getName()));
      assertEquals(false, result.isSuccessful(p2.getName()));
    } else {
      System.err.println("SimulatorUnit on localhost:1337 wasn't available. Skipping test.");
    }
  }
}
