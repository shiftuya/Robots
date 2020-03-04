package simulator

import groovy.transform.CompileStatic

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
            return "g:" + goalX + ":" + goalY + "\tcoord:" + x + ":" + y;
        }
    }
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
        return 0
    }

    @Override
    String getSensorReadings(int robotId, String sensor) {
        Robot rb = robots.get(robotId)
        //System.out.println(rb.debugString())
        String result
        switch (sensor) {
            case "x":
                result = String.valueOf(rb.x)
                break
            case "y":
                result = String.valueOf(rb.y)
                break
            default:
                return null
        }
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
            for (def r : robots) {
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
}
