package ru.nsu.fit.markelov;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.Test;
import ru.nsu.fit.markelov.interfaces.Player;
import ru.nsu.fit.markelov.interfaces.SimulationResult;
import ru.nsu.fit.markelov.objects_hardcoded.PlayerHardcoded;
import ru.nsu.fit.markelov.simulator.HardcodedSimulatorManager;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SimulatorIntegralTest {

  @Test
  public void testCorrectSolutions() {
    Player p1 = new PlayerHardcoded();
    Player p2 = new PlayerHardcoded();
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
    argMap.put(p2, new String(correctSolution));
    HardcodedSimulatorManager hsm = new HardcodedSimulatorManager();
    SimulationResult result = hsm.runSimulation("simple_plane", 0, argMap);
    assertEquals(0, result.getId());
    assertTrue(result.isSuccessful(p1.getName()));
    assertTrue(result.isSuccessful(p2.getName()));
  }
}
