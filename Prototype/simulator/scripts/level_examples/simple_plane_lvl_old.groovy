package simulator

import groovy.transform.CompileStatic
import simulator.playback.Playback
import simulator.playback.PlaybackCreator
import simulator.playback.Vec3Proto

@CompileStatic
class simple_plane_lvl implements Level {
    class Robot implements Comparable<Robot> {
        public int x, y, goalX, goalY;
        public double finishTime;
        public String currentAction
        boolean broken

        Robot(int _x, int _y, int gx, int gy) {
            x = _x
            y = _y
            goalX = gx
            goalY = gy
            finishTime = 0
            broken = false
        }

        boolean checkRobot() {
            return x == goalX && y == goalY
        }

        @Override
        int compareTo(Robot robot) {
            return finishTime - robot.finishTime
        }

        String debugString() {
            return "g:" + goalX + ":" + goalY + "\tcoords:" + x + ":" + y;
        }
    }
    Logger logger
    PlaybackCreator pbc
    ArrayList<Robot> robots
    int playerCount
    double virtualTime
    double timeout

    simple_plane_lvl(int _playerCount) {
        playerCount = _playerCount
        robots = new ArrayList<>();
        Random rnd = new Random()
        int len = 100;
        int goal_x = rnd.nextInt(101)
        int goal_y = len - goal_x
        if (rnd.nextBoolean()) {
            goal_x = -goal_x
        }
        if (rnd.nextBoolean()) {
            goal_x = -goal_x
        }
        for (int i = 0; i < playerCount; i++) {
            robots.add(new Robot(0,
                    0,
                    goal_x,
                    goal_y))
        }
        virtualTime = 0
        timeout = 120
        logger = new Logger(playerCount)
        pbc = new PlaybackCreator(_playerCount, 4)
        for (int i = 0; i < _playerCount; i++) {
            pbc.updateColor(i, 0x00ff00)
            pbc.updateDimension(i, new Vec3Proto(10f, 10f, 10f))
            pbc.updatePosition(i, new Vec3Proto(robots[i].x * 10, 0, robots[i].y * 10))
        }
        pbc.updateColor(playerCount, 0xff00ff)
        pbc.updateDimension(playerCount, new Vec3Proto(15f, 5f, 15f))
        pbc.updatePosition(playerCount, new Vec3Proto(goal_x * 10, 0, goal_y * 10))

        pbc.updateColor(playerCount + 1, 0xff0000)
        pbc.updateDimension(playerCount + 1, new Vec3Proto(20f, 2f, 2f))
        pbc.updatePosition(playerCount + 1, new Vec3Proto(0, 0, 0))
        pbc.updateColor(playerCount + 2, 0x00ff00)
        pbc.updateDimension(playerCount + 2, new Vec3Proto(2f, 20f, 2f))
        pbc.updatePosition(playerCount + 2, new Vec3Proto(0, 0, 0.0f))
        pbc.updateColor(playerCount + 3, 0x0000ff)
        pbc.updateDimension(playerCount + 3, new Vec3Proto(2f, 2f, 20f))
        pbc.updatePosition(playerCount + 3, new Vec3Proto(0, 0, 0))
    }

    @Override
    int getPlayerCount() {
        return playerCount
    }

    @Override
    int setAction(int robotId, String action, double time) {
        Robot rb = robots.get(robotId)
        rb.currentAction = action
        rb.finishTime += time
        logger.writeLog(robotId, "\tAction: " + action + "\n\tDuration: " + time + "\n")
        return 0
    }

    @Override
    Object getSensorReadings(int robotId, String sensor) {
        Robot rb = robots.get(robotId)
        logger.writeLog(robotId, "Sensor \"" + sensor + "\":")
        String result
        switch (sensor) {
            case "x":
                result = String.valueOf(rb.x)
                break
            case "y":
                result = String.valueOf(rb.y)
                break
            default:
                logger.writeLog(robotId, "null")
                return null
        }
        logger.writeLog(robotId, result);
        return result
    }

    @Override
    String getGoal(int robotId) {
        Robot rb = robots.get(robotId)
        return "" + rb.goalX + " " + rb.goalY
    }

    @Override
    boolean checkGoal(int robotId) {
        return robots.get(robotId).checkRobot()
    }

    @Override
    int getNextRobotId() {
        int next = -1

        double ft = timeout * 10
        for (int i = 0; i < playerCount; i++) {
            Robot rb = robots.get(i)
            if (!rb.broken && rb.finishTime < ft) {
                ft = rb.finishTime
                next = i
            }
        }
        return next
    }

    @Override
    boolean simulateUntilRFT(int robotId) {
        Robot rb = robots.get(robotId)
        double ft = rb.finishTime
        if (ft >= timeout) {
            return false
        }

        while (virtualTime < ft) {
            // System.out.println("ft:" + ft + " vt:" + virtualTime)
            for (int i = 0; i < robots.size(); i++) {
                Robot r = robots[i]
                switch (r.currentAction) {
                    case "up":
                        r.y += (int) (ft - virtualTime)
                        break
                    case "down":
                        r.y -= (int) (ft - virtualTime)
                        break
                    case "left":
                        r.x -= (int) (ft - virtualTime)
                        break
                    case "right":
                        r.x += (int) (ft - virtualTime)
                        break
                    default:
                        r.setBroken(true)
                }
            }
            virtualTime = ft
            pbc.setTime(virtualTime.intValue() * 10)
            for (int i = 0; i < robots.size(); i++) {
                def r = robots[i]
                pbc.updatePosition(i, new Vec3Proto(r.x * 10, 0, r.y * 10))
            }
        }
        return true
    }

    @Override
    double getVirtualTime() {
        return virtualTime
    }

    @Override
    void breakRobot(int robotId) {
        robots.get(robotId).broken = true
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
        for (int i = 0; i < robots.size(); i++) {
            def r = robots[i]
            pbc.updatePosition(i, new Vec3Proto(r.x * 10, 0, r.y * 10))
        }
        return pbc.getPlayback()
    }
}
