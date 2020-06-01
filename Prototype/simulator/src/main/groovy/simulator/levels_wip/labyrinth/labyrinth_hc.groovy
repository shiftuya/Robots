//
// SOLUTION NOT READY :-(
//

// 0 = unknown
// 1 = empty
// 2 = wall
// 3 = coin
String cellToString(int cell) {
    switch (cell) {
        case 0:
            return "Unknown"
        case 1:
            return "Empty"
        case 2:
            return "Wall"
        case 3:
            return "Coin"
        default:
            return "Error"
    }
}

class Point {
    int x, y, fromX, fromY, distance

    Point(int x, int y, int fromX, int fromY, int distance) {
        this.x = x
        this.y = y
        this.fromX = fromX
        this.fromY = fromY
        this.distance = distance
    }
}

class MyMemory {
    public int coinsLeft, coinsKnown
    public ArrayList<ArrayList<Integer>> map
    public int turn, x, y
    public LinkedList<Point> queue


    MyMemory() {
        map = new ArrayList<>()
        for (int i = 0; i < 80; i++) {
            ArrayList<Integer> row = new ArrayList<>()
            for (int j = 0; j < 80; j++) {
                row.add(0)
            }
            map.add(row)
            x = 40
            y = 40
        }
    }
}


void updateFromSensor(int dx, int dy, MyMemory memory) {
    String sensorRes = "";
    if (dx == 1) {
        sensorRes = level.getSensorReadings("right")
    }
    if (dx == -1) {
        sensorRes = level.getSensorReadings("left")
    }
    if (dy == 1) {
        sensorRes = level.getSensorReadings("down")
    }
    if (dy == -1) {
        sensorRes = level.getSensorReadings("up")
    }
    String[] tokens = sensorRes.split()
    int dist = tokens[1] as int
    int x = memory.x + dist * dx
    int y = memory.y + dist * dy
    for (int i = 0; i < dist; i++) {
        memory.map[memory.y + dy * i][memory.x + dx * i] = 1
    }
    switch (tokens[0]) {
        case "Wall":
            memory.map[y][x] = 2
            break
        case "Coin":
            memory.map[y][x] = 3
            level.writeLog("See a coin at ${x}:${y}")
            memory.coinsKnown++
            break
    }

}

int ray(int dx, int dy, MyMemory memory) {
    int x = memory.x
    int y = memory.y
    int dist = 0
    while (memory.map.get(y).get(x) == 1) {
        x += dx
        y += dy
        dist++
    }
    return memory.map.get(y).get(x)
}

void addToQueue(MyMemory memory, HashSet<AbstractMap.SimpleEntry<Integer, Integer>> scanned, Point from, AbstractMap.SimpleEntry<Integer, Integer> current) {
    int x = current.key
    int y = current.value
    if (!scanned.contains(current) && memory.map[y][x] != 2) {
        //level.writeLog("Added ${x}:${y} ${cellToString(memory.map[y][x])}")
        memory.queue.addLast(new Point(current.key, current.value, from.x, from.y, from.distance + 1))
        scanned.add(current)
    }
}

Point bfs(MyMemory memory) {
    HashSet<AbstractMap.SimpleEntry<Integer, Integer>> scanned = new HashSet<>()
    ArrayList<Point> passed = new ArrayList<>();
    Point target = null;
    memory.queue = new LinkedList<>()
    memory.queue.add(new Point(memory.x, memory.y, memory.x, memory.y, 0))
    scanned.add(new AbstractMap.SimpleEntry<Integer, Integer>(memory.x, memory.y))
    while (!memory.queue.isEmpty()) {
        Point current = memory.queue.pollFirst()
        passed.add(current)
        def left = new AbstractMap.SimpleEntry<Integer, Integer>(current.x - 1, current.y)
        def right = new AbstractMap.SimpleEntry<Integer, Integer>(current.x + 1, current.y)
        def up = new AbstractMap.SimpleEntry<Integer, Integer>(current.x, current.y - 1)
        def down = new AbstractMap.SimpleEntry<Integer, Integer>(current.x, current.y + 1)
        addToQueue(memory, scanned, current, up)
        addToQueue(memory, scanned, current, down)
        addToQueue(memory, scanned, current, left)
        addToQueue(memory, scanned, current, right)
        if (memory.coinsKnown == 0 && memory.map[current.y][current.x] == 0) {
            target = current
            level.writeLog("Found unknown at ${target.x}:${target.y}")
            break
        } else if (memory.map[current.y][current.x] == 3) {
            target = current
            level.writeLog("Found coin at ${target.x}:${target.y}")
            break
        }
    }
    while (target.fromX != memory.x || target.fromY != memory.y) {
        //level.writeLog("To go to ${target.x}:${target.y} I must go to ${target.fromX}:${target.fromY}")
        target = passed.stream().filter({ p -> p.x == target.fromX && p.y == target.fromY }).findFirst().orElse(new Point(memory.x, memory.y, memory.x, memory.y, 0))
    }
    return target
}

void checkIfKnown(int dx, int dy, MyMemory memory) {
    if (ray(dx, dy, memory) == 0) {
        updateFromSensor(dx, dy, memory)
    }
}

void recon(MyMemory memory) {
    checkIfKnown(1, 0, memory)
    checkIfKnown(-1, 0, memory)
    checkIfKnown(0, -1, memory)
    checkIfKnown(0, 1, memory)
}

String turn(MyMemory memory) {
    if (memory.map[memory.y][memory.x] == 3) {
        level.writeLog("Got a coin!")
        memory.coinsKnown--
    }
    memory.map[memory.y][memory.x] = 1

    recon(memory)
    try {
        level.writeLog("Known coins: ${memory.coinsKnown}")
        Point target = bfs(memory)

        dx = target.x - memory.x
        dy = target.y - memory.y
        memory.x += dx
        memory.y += dy
        if (dx == 1) {
            return "right 1"
        }
        if (dx == -1) {
            return "left 1"
        }
        if (dy == -1) {
            return "up 1"
        }
        if (dy == 1) {
            return "down 1"
        }
        return "sleep 1"
    } catch (Exception ignore) {
        return "sleep 1"
    }
}


if (memory.class.name != "MyMemory") {
    level.writeLog(memory.class.name)
    memory = new MyMemory()
    memory.turn = 0
    level.writeLog("INITIALIZING")
}
memory.turn++

return turn((MyMemory) memory)
