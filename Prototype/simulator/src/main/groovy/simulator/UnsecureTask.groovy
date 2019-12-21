package simulator

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.util.concurrent.TimeoutException

class UnsecureTask implements Task {

    ArrayList<String> solutions
    ArrayList<Script> scripts
    Level lvl
    long timeForScripts
    long lvlTime

    UnsecureTask(String jsonData) {
        def jsonSlurp = new JsonSlurper()
        def jsonObject = jsonSlurp.parseText(jsonData)
        solutions = jsonObject.solutions
        String lvlName = jsonObject.level
        def gcl = new GroovyClassLoader()
        def levelClass = gcl.parseClass(new File("src/main/groovy/simulator/" + lvlName + "_lvl.groovy"))
        lvl = (Level) levelClass.newInstance(solutions.size())
        scripts = new ArrayList<>()
        lvlTime = 0
        timeForScripts = 0
    }

    @CompileStatic
    String run() {
        long startRunTime = System.currentTimeMillis()
        String result = ""
        assert lvl != null
        boolean finished = false;
        def binding = new Binding()
        binding.setVariable("level", lvl)
        binding.setVariable("action", new String())
        println("Robot count:" + solutions.size())
        for (int i = 0; i < solutions.size(); i++) {
            def script = new GroovyShell(binding)
            script.setVariable("robotId", i) // TODO: Change to runtime id selection
            scripts.add(script.parse(solutions.get(i)))

        }
        println("Parsed solutions")
        long start
        long end
        while (!finished) {
            start = System.currentTimeMillis()
            int robotId = lvl.getNextRobotId()
            end = System.currentTimeMillis()
            lvlTime += end - start
            println("Id:" + robotId)
            if (robotId < 0) {
                return JsonOutput.toJson([timeout: false, broken: true])
            }
            start = System.currentTimeMillis()
            if (!lvl.simulateUntilRFT(robotId)) {
                return JsonOutput.toJson([timeout: true, broken: false])
            }
            end = System.currentTimeMillis()
            lvlTime += end - start
            println("Set id")
            println("VT: " + lvl.getVirtualTime())
            println("Goal: " + lvl.getGoal(robotId))
            println("x: " + lvl.getSensorReadings(robotId, "x"))
            println("y: " + lvl.getSensorReadings(robotId, "y"))
            String action
            double duration
            try {
                start = System.currentTimeMillis()
                String cmd = scripts.get(robotId).evaluate(solutions.get(robotId))
                end = System.currentTimeMillis()
                timeForScripts += end - start
                def cmds = cmd.split()
                action = cmds[0]
                duration = cmds[1].toDouble()
            } catch (Exception e) {
                println("Broken script")
                println(e)

            }
                println("Run script")
                println("Action: " + action + "\tDuration: " + duration)
            start = System.currentTimeMillis()
            lvl.setAction(robotId, action, duration)
            end = System.currentTimeMillis()
            lvlTime += end - start
            println(action)

            if (lvl.checkGoal(robotId)) {
                ArrayList<Boolean> passed = new ArrayList<>()
                for (int i = 0; i < solutions.size(); i++) {
                    passed.add(lvl.checkGoal(i))
                }
                result = JsonOutput.toJson([timeout: false, broken: false, results: passed])
                break
            }


        }
        println("Task time: "+(System.currentTimeMillis()-startRunTime))
        println("Level time:" + lvlTime)
        println("Script time:" + timeForScripts)
        return result
    }
}
/*

{
level:"simple_plane",
solutions:["some code","other code"]
}
 */