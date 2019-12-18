package simulator

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import lobbymanager.Player
import lobbymanager.SimulationResult


class HardcodedSimulatorManager implements SimulatorManager {
    private ArrayList<String> urls;

    public HardcodedSimulatorManager() {
        urls = new ArrayList<>();
        urls.add("http://localhost:1337")
    }

    @Override
    boolean addSimulator(String url) {
        return false
    }

    @Override
    boolean removeSimulator(String url) {
        return false
    }

    @Override
    ArrayList<String> getSimulatorsList() {
        return urls
    }

    @Override
    Map<Player, SimulationResult> runSimulation(String levelId, Map<Player, String> solutions) {
        ArrayList<Map.Entry<Player, String>> entryList = ArrayList<Map.Entry<Player, String>>(solutions.entrySet())
        ArrayList<String> sol = new ArrayList<>();
        entryList.stream().peek({ e -> sol.add(e.value) })

        String request = JsonOutput.toJson([level: levelId, solutions: sol]);

        def post = new URL("http://localhost:1337/simulate").openConnection();
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.getOutputStream().write(request.getBytes("UTF-8"));
        def postRC = post.getResponseCode;
        String response = post.getInputStream().getText();
        def jsonSlurper = new JsonSlurper()
        def respObj = jsonSlurper.parseText(response)

        Map<Player, SimulationResult> results = new HashMap<>()
        Date now = new Date()
        if (respObj.timeout || respObj.broken) {
            for (Map.Entry<Player, String> entry : entryList) {
                results.put(entry.key, new SimResultPlaceholder(now, false))
            }
        } else {
            def passed = respObj.results
            for (int i = 0; i < entryList.size(); i++) {
                results.put(entry.key, new SimResultPlaceholder(now, passed.get(i)))
            }
        }
        return results
    }
}
