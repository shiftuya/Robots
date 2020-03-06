package simulator

class Logger {
    private ArrayList<StringBuilder> logs

    Logger(int size) {
        logs = new ArrayList<>()
        for (int i = 0; i < size; i++) {
            logs.add(new StringBuilder())
        }
    }

    void writeLog(int robotId, String str) {
        logs.get(robotId).append(str + System.lineSeparator())
    }

    String getLog(int robotId) {
        return logs.get(robotId).toString()
    }
}
