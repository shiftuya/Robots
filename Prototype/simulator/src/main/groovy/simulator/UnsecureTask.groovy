package simulator

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.lang.Binding


class UnsecureTask implements Task {

    ArrayList<String> solutions
    ArrayList<Script> scripts
    ArrayList<StringBuilder> loggers
    Level lvl
    long timeForScripts
    long lvlTime
    boolean printLog

    private writeLog(int robotId, String data) {
        loggers.get(robotId).append(data)
        println(robotId + ": " + data)
    }

    private ArrayList<String> getLogs() {
        ArrayList<String> res = new ArrayList<>()
        for (StringBuilder srtB : loggers) {
            res.add(srtB.toString())
        }
        return res
    }

    UnsecureTask(String jsonData) {
        def jsonSlurp = new JsonSlurper()
        def jsonObject = jsonSlurp.parseText(jsonData)
        solutions = jsonObject.solutions
        String lvlName = jsonObject.level
        def gcl = new GroovyClassLoader()
        def levelClass = gcl.parseClass(new File("src/main/groovy/simulator/" + lvlName + "_lvl.groovy"))
        lvl = (Level) levelClass.newInstance(solutions.size())
        scripts = new ArrayList<>()
        loggers = new ArrayList<>()
        lvlTime = 0
        timeForScripts = 0
    }

    @CompileStatic
    String run() {
        long startRunTime = System.currentTimeMillis()
        String result = ""
        assert lvl != null
        boolean finished = false;

        println("===========================\nRobot count:" + solutions.size())
        for (int i = 0; i < solutions.size(); i++) {
            Binding binding = new Binding()
            binding.setVariable("level", lvl)
            binding.setVariable("action", new String())
            binding.setVariable("robotId",i)
            def script = new GroovyShell(binding)
            //script.setVariable("robotId", i) // TODO: Change to runtime id selection
            scripts.add(script.parse(solutions.get(i)))
            loggers.add(new StringBuilder())
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
                for (int i = 0; i < solutions.size(); i++) {
                    writeLog(i, "\nAll robots are broken, ending simulation\n")
                }
                return JsonOutput.toJson([timeout: false, broken: true, logs: getLogs()])
            }
            start = System.currentTimeMillis()
            if (!lvl.simulateUntilRFT(robotId)) {
                return JsonOutput.toJson([timeout: true, broken: false, logs: getLogs()])
            }
            end = System.currentTimeMillis()
            lvlTime += end - start
            writeLog(robotId, "Virtual time: " + lvl.getVirtualTime() + "\n")
            writeLog(robotId, "\tGoal: (" + lvl.getGoal(robotId) + ")\n")
            String action
            double duration
            try {
                start = System.currentTimeMillis()
                writeLog(robotId, "\tCoordinates: (" + lvl.getSensorReadings(robotId, "x") + ";" + lvl.getSensorReadings(robotId, "y") + ")\n")
                String cmd = scripts.get(robotId).evaluate(solutions.get(robotId))
                //writeLog(robotId, "\tCoordinates: (" + lvl.getSensorReadings(robotId, "x") + ";" + lvl.getSensorReadings(robotId, "y") + ")\n")
                end = System.currentTimeMillis()
                timeForScripts += end - start
                def cmds = cmd.split()
                action = cmds[0]
                duration = cmds[1].toDouble()
            } catch (Exception e) {
                println("Broken script")
                writeLog(robotId, "Script has broken:\n")
                writeLog(robotId, e.toString())
                lvl.breakRobot(robotId)
                continue
            }
            println("Run script")
            writeLog(robotId," x: "+ lvl.getSensorReadings(robotId,"x"));
            writeLog(robotId," y: "+ lvl.getSensorReadings(robotId,"y"));
            writeLog(robotId, "\tAction: " + action + "\n\tDuration: " + duration+"\n")
            start = System.currentTimeMillis()
            lvl.setAction(robotId, action, duration)
            end = System.currentTimeMillis()
            lvlTime += end - start
            println(action)

            if (lvl.checkGoal(robotId)) {
                ArrayList<Boolean> passed = new ArrayList<>()
                for (int i = 0; i < solutions.size(); i++) {
                    passed.add(lvl.checkGoal(i))
                    writeLog(i,"First robot reached goal.\n")
                    if(lvl.checkGoal(i)){
                        writeLog(i,"Simulation result: SUCCESS.\n")
                    }else{
                        writeLog(i,"Simulation result: FAIL.\n")
                    }
                }
                result = JsonOutput.toJson([timeout: false, broken: false, results: passed, logs: getLogs()]);
                break
            }


        }
        println("Task time: " + (System.currentTimeMillis() - startRunTime))
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