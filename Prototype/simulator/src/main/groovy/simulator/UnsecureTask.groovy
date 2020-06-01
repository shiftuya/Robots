package simulator

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

class UnsecureTask implements Task {

    ArrayList<String> solutions
    ArrayList<Script> scripts
    ArrayList<StringBuilder> loggers
    Level lvl
    long timeForScripts
    long lvlTime


    @CompileStatic
    private ArrayList<String> getLogs() {
        ArrayList<String> res = new ArrayList<>()
        for (int i = 0; i < solutions.size(); i++) {
            res.add(lvl.getLog(i))
        }
        return res
    }

    UnsecureTask(String jsonData, LevelSrcManager mng) {
        def jsonSlurp = new JsonSlurper()
        def jsonObject = jsonSlurp.parseText(jsonData)
        solutions = jsonObject.solutions
        String lvlName = jsonObject.level
        def levelClass = mng.getLevel(lvlName)
        if (levelClass != null)
            lvl = (Level) levelClass.newInstance(solutions.size(), "levels/" + lvlName + "/")

        //lvl = (Level)  new labyrinth(solutions.size(),"levels/"+lvlName+"/")
        scripts = new ArrayList<>()
        loggers = new ArrayList<>()
        lvlTime = 0
        timeForScripts = 0
    }

    @CompileStatic
    String run() {
        long startRunTime = System.currentTimeMillis()
        String result = ""
        if (lvl == null) {
            throw new RuntimeException("No level available")
        }
        boolean finished = false;

        for (int i = 0; i < solutions.size(); i++) {
            Binding binding = new Binding()
            SensorReadable sens = new RobotSensors(lvl, i)
            binding.setVariable("level", sens)
            binding.setVariable("memory", new Object())
            def script = new GroovyShell(binding)
            scripts.add(script.parse(solutions.get(i)))
        }
        long start
        long end
        while (!finished) {
            start = System.currentTimeMillis()
            int robotId = lvl.getNextRobotId()
            end = System.currentTimeMillis()
            lvlTime += end - start
            if (robotId < 0) {
                for (int i = 0; i < solutions.size(); i++) {
                    lvl.writeLog(i, "\nAll robots are broken, ending simulation\n")
                }
                return JsonOutput.toJson([timeout: false, broken: true, logs: getLogs(), playback: lvl.getPlayback()])
            }
            start = System.currentTimeMillis()
            if (!lvl.simulateUntilRFT(robotId)) {
                return JsonOutput.toJson([timeout: true, broken: false, logs: getLogs(), playback: lvl.getPlayback()])
            }
            end = System.currentTimeMillis()
            lvlTime += end - start
            String action
            double duration
            try {
                start = System.currentTimeMillis()
                String cmd = scripts.get(robotId).run()
                end = System.currentTimeMillis()
                timeForScripts += end - start
                def cmds = cmd.split()
                action = cmds[0]
                duration = cmds[1].toDouble()
                println(lvl.getLog(0))
            } catch (Exception e) {
                println("Broken script")
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                lvl.writeLog(robotId, "Script has broken:\n")
                lvl.writeLog(robotId, sw.toString())
                lvl.breakRobot(robotId)
                continue
            }
            start = System.currentTimeMillis()
            lvl.setAction(robotId, action, duration)
            end = System.currentTimeMillis()
            lvlTime += end - start

            if (lvl.checkGoal(robotId)) {
                ArrayList<Boolean> passed = new ArrayList<>()
                for (int i = 0; i < solutions.size(); i++) {
                    passed.add(lvl.checkGoal(i))
                    lvl.writeLog(i, "First robot reached goal.\n")
                    if (lvl.checkGoal(i)) {
                        lvl.writeLog(i, "Simulation result: SUCCESS.\n")
                    } else {
                        lvl.writeLog(i, "Simulation result: FAIL.\n")
                    }
                }
                result = JsonOutput.toJson([timeout: false, broken: false, results: passed, logs: getLogs(), playback: lvl.getPlayback()]);
                break
            }
        }
        return result
    }

}