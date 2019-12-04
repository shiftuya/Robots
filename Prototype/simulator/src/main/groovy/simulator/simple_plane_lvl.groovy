package simulator

class simple_plane_lvl implements level {
    class Robot implements Comparable<Robot> {
        public int x, y, goalX, goalY, finishTime;

        Robot(int _x, _y, gx, gy) {
            x = _x
            y = _y
            goalX = gx
            goalY = gy
            finishTime = 0
        }

        boolean checkRobot() {
            return x == goalX && y == goalY
        }

        @Override
        int compareTo(Robot robot) {
            return finishTime - robot.finishTime
        }
    }
    ArrayList<Robot> robots
    int playerCount

    simple_plane_lvl(int _playerCount) {
        playerCount = _playerCount
        robots = new ArrayList<>();
        Random rnd = new Random()
        for (int i = 0; i < playerCount; i++) {
            robots.add(new Robot(rnd.nextInt(101) - 50,
                    rnd.nextInt(101) - 50,
                    rnd.nextInt(101) - 50,
                    rnd.nextInt(101) - 50))
        }
    }

    @Override
    int getPlayerCount() {
        return playerCount
    }

    @Override
    int performAction(int robotId, String action) {
        Robot rb = robots.get(robotId)
        switch (action) {
            case "up":
                rb.y++
                break
            case "down":
                rb.y--
                break
            case "left":
                rb.x--
                break
            case "right":
                rb.x++
                break
            default:
                return 1
        }
        return 0
    }

    @Override
    String getSensorReadings(int robotId, String sensor) {
        Robot rb = robots.get(robotId)
        String result
        switch (action) {
            case "x":
                result = rb.y.toString()
                break
            case "y":
                result = rb.y.toString()
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
        int next = 0
        int ft = robots.get(0).finishTime
        for (int i = 1; i < playerCount; i++) {
            if (robots.get(i).finishTime < ft) {
                ft = robots.get(i).finishTime
                next = i
            }
        }
        return next
    }
}
