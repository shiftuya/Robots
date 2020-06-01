package simulator.levels_wip.labyrinth

import groovy.transform.CompileStatic
import simulator.Level
import simulator.Logger
import simulator.playback.Playback
import simulator.playback.PlaybackCreator
import simulator.playback.Vec3Proto
import java.util.AbstractMap

@CompileStatic
class labyrinth implements Level {
    class Robot implements Comparable<Robot> {
        public int x, y, coinsLeft;
        public double finishTime;
        public String currentAction
        boolean broken

        Robot(int _x, int _y, int coins) {
            x = _x
            y = _y
            coinsLeft = coins
            finishTime = 0
            broken = false
        }

        boolean checkRobot() {
            return coinsLeft == 0
        }

        @Override
        int compareTo(Robot robot) {
            return finishTime - robot.finishTime
        }
    }

    Logger logger
    PlaybackCreator pbc
    Robot robot
    int playerCount
    double virtualTime
    double timeout
    float scale = 10

    ArrayList<ArrayList<Character>> map
    HashMap<Map.Entry<Integer, Integer>, Integer> pbId;

    labyrinth(int _playerCount, String path) {
        if (_playerCount != 1) {
            throw new RuntimeException("Invalid player count")
        }
        Random rnd = new Random()
        int start_x = 0, start_y = 0, coins = 0;
        map = new ArrayList<>()
        pbId = new HashMap<>()
        int objCount = 0
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/map1.dat"))
            String line
            while ((line = br.readLine()) != null) {
                ArrayList<Character> row = new ArrayList<>()
                for (int i = 0; i < line.size(); i++) {
                    char c = line.charAt(i)
                    if (c == ('S' as char)) {
                        start_y = map.size()
                        start_x = i
                        row.add('.' as char)
                    } else {
                        if (c == ('C' as char)) {
                            coins++
                            objCount++
                            pbId.put(new AbstractMap.SimpleEntry<Integer, Integer>(i, map.size()), objCount)
                        }
                        if (c == ('X' as char)) {
                            objCount++
                            pbId.put(new AbstractMap.SimpleEntry<Integer, Integer>(i, map.size()), objCount)
                        }
                        row.add(c)
                    }
                }
                map.add(row)
            }
        } catch (IOException ignored) {
            throw new IOException("Resource file was inaccessible")
        }

        robot = new Robot(start_x, start_y, coins)
        virtualTime = 0
        timeout = 500
        logger = new Logger(1)
        pbc = new PlaybackCreator(1, objCount)
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                if (map[y][x] == 'C' as char) {
                    pbc.updateDimension(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), new Vec3Proto(0.5 * scale as float, 0.3 * scale as float, 0.5 * scale as float))
                    pbc.updatePosition(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), new Vec3Proto(x * scale as float, 0, y * scale as float))
                    pbc.updateColor(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), 0xebe528)
                }
                if (map[y][x] == 'X' as char) {
                    pbc.updateDimension(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), new Vec3Proto(1 * scale as float, 1 * scale as float, 1 * scale as float))
                    pbc.updatePosition(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), new Vec3Proto(x * scale as float, 0, y * scale as float))
                    pbc.updateColor(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), 0x625f8e)
                }
            }
        }
        pbc.updateColor(0, 0x00ff00)
        pbc.updateDimension(0, new Vec3Proto(0.8 * scale as float, 1.4 * scale as float, 0.8 * scale as float))
        pbc.updatePosition(0, new Vec3Proto(robot.x * scale as float, 0, robot.y * scale as float))
    }

    @Override
    int getPlayerCount() {
        return playerCount
    }

    @Override
    int setAction(int robotId, String action, double time) {
        Robot rb = robot
        rb.currentAction = action
        rb.finishTime += time
        logger.writeLog(robotId, "\tAction: " + action + "\n\tDuration: " + time + "\n")
        return 0
    }

    String sensorR(int x, int y, int dx, int dy) {
        int passed = 0;
        while (map[y][x] == '.' as char) {
            y += dy
            x += dx
            passed++
        }
        String result = ""
        if (map[y][x] == 'C' as char) {
            result += "Coin "
        } else {
            result += "Wall "
            pbc.updateColor(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)), 0x1f1e30)
        }
        return result + String.valueOf(passed)
    }

    @Override
    Object getSensorReadings(int robotId, String sensor) {
        Robot rb = robot
        logger.writeLog(robotId, "Sensor \"" + sensor + "\":")
        String result
        switch (sensor) {
            case "up":
                result = sensorR(rb.x, rb.y, 0, -1)
                break
            case "down":
                result = sensorR(rb.x, rb.y, 0, 1)
                break
            case "left":
                result = sensorR(rb.x, rb.y, -1, 0)
                break
            case "right":
                result = sensorR(rb.x, rb.y, 1, 0)
                break
            default:
                logger.writeLog(robotId, "null")
                return null
        }
        pbc.updateSensor(robotId, sensor, result)
        logger.writeLog(robotId, result);
        return result
    }

    @Override
    String getGoal(int robotId) {
        Robot rb = robot
        return "" + rb.coinsLeft
    }

    @Override
    boolean checkGoal(int robotId) {
        return robot.checkRobot()
    }

    @Override
    int getNextRobotId() {
        int next = -1

        double ft = timeout * 10
        Robot rb = robot
        if (!rb.broken && rb.finishTime < ft) {
            ft = rb.finishTime
            next = 0
        }
        return next
    }

    boolean processPosition(Robot rb) {
        if (map[rb.y][rb.x] == 'C' as char) {
            rb.coinsLeft--
            map[rb.y][rb.x] = new Character('.' as char)
            pbc.breakpoint(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(rb.x, rb.y)))
            pbc.updateDimension(pbId.get(new AbstractMap.SimpleEntry<Integer, Integer>(rb.x, rb.y)), new Vec3Proto(0, 0, 0))
            return true
        }
        if (map[rb.y][rb.x] == '.' as char) {
            return true
        }
        pbc.updateColor(0, 0xff0000)
        pbc.updateDimension(0, new Vec3Proto(scale, 2 * scale as float, scale))
        rb.broken = true
        return false
    }

    @Override
    boolean simulateUntilRFT(int robotId) {
        Robot r = robot
        double ft = r.finishTime
        if (ft >= timeout) {
            return false
        }

        while (virtualTime < ft && !r.broken) {
            // System.out.println("ft:" + ft + " vt:" + virtualTime)

            switch (r.currentAction) {
                case "up":
                    r.y--
                    break
                case "down":
                    r.y++
                    break
                case "left":
                    r.x--
                    break
                case "right":
                    r.x++
                    break
                case "sleep":
                    break
                default:
                    r.setBroken(true)
            }
            virtualTime++
            processPosition(r)
            pbc.setTime(virtualTime.intValue() * 10)
            pbc.updatePosition(0, new Vec3Proto(r.x * scale as float, 0, r.y * scale as float))

        }
        return true
    }

    @Override
    double getVirtualTime() {
        return virtualTime
    }

    @Override
    void breakRobot(int robotId) {
        robot.broken = true
        logger.writeLog(robotId, "Robot was broken")
        pbc.updateColor(robotId, 0xff0000)
    }

    @Override
    void writeLog(int robotId, String str) {
        logger.writeLog(robotId, str)
    }

    @Override
    String getLog(int robotId) {
        return logger.getLog(robotId)
    }

    @Override
    Playback getPlayback() {
        pbc.setTime(virtualTime.intValue() * 10 + 60)
        def r = robot
        pbc.updatePosition(0, new Vec3Proto(r.x * scale as float, 0, r.y * scale as float))
        return pbc.getPlayback()
    }
}
