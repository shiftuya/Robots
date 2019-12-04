package simulator

import groovy.json.JsonSlurper

class Task {

    ArrayList<String> solutions
    def lvl

    Task(String jsonData) {
        def jsonSlurper = new JsonSlurper()
        def jsonObject = jsonSlurper.parseText(jsonData)
        solutions = jsonObject.solutions
        String lvlName = jsonObject.level
        def gcl = new GroovyClassLoader()
        def levelClass = gcl.parseClass(lvlName + "_lvl.groovy")
        lvl = (level) levelClass.newInstance(solutions.size())
    }

    String run() {
        assert lvl != null

    }
}