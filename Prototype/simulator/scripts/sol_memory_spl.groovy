// CORRECT
// level is defined in binding
public class Memory {
    int x, y, gx, gy
}

if (memory.class.name != "Memory") {
    memory = new Memory()
    String goalStr = level.getGoal()
    def goals = goalStr.split(" ")
    memory.gx = goals[0] as int
    memory.gy = goals[1] as int
    String xStr = level.getSensorReadings("x")
    String yStr = level.getSensorReadings("y")
    memory.y = yStr as int
    memory.x = xStr as int
    level.writeLog("INITIALIZING")
}
int dur
if (memory.x < memory.gx) {
    dur = memory.gx - memory.x
    memory.x = memory.gx
    return "right " + dur
}
if (memory.x > memory.gx) {
    dur = memory.x - memory.gx
    memory.x = memory.gx
    return "left " + dur
}
if (memory.y < memory.gy) {
    dur = memory.gy - memory.y
    memory.y = memory.gy
    return "up " + dur
}
if (memory.y > memory.gy) {
    dur = memory.y - memory.gy
    memory.y = memory.gy
    return "down " + dur
}
return "stay 1"
