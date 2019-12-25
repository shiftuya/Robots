package lobbymanager

import spock.lang.Specification

class HardcodedSMTest extends Specification {
    def "send solution"() {
        def simManager = new HardcodedSimulatorManager()
        given:
        HashMap<Player, String> solutions = new HashMap<>()
        PlayerClass p1 = new PlayerClass(0, "Vovan")
        PlayerClass p2 = new PlayerClass(1, "Pahan")
        String correctSolution = '''package simulator
String goalStr = level.getGoal(robotId)
def goals = goalStr.split()                                              
int gx = goals[0].toInteger()                                            
int gy = goals[1].toInteger()                                            
String xStr = level.getSensorReadings(robotId, "x")                      
String yStr = level.getSensorReadings(robotId, "y")                      
def y = yStr.toInteger()                                                 
def x = xStr.toInteger()                                                 
if (x < gx)
    return "right " + (gx - x)
    if (x > gx)                                                              
    return "left " + (x - gx)                                            
if (y < gy)                                                              
    return "up " + (gy - y)                                              
if (y > gy)                                                              
    return "down " + (y - gy)                                            
return "stay 1"'''
        solutions.put(p1, new String(correctSolution))
        solutions.put(p2, new String(correctSolution))
        when:
        HashMap<Player, SimulationResult> sr = simManager.runSimulation("simple_plane", solutions)
        for (def entry : sr.entrySet())
            println("" + entry.key.id + "|" + entry.value.successful)
        then:
        sr.get(p1).successful
        sr.get(p2).successful
    }
}
