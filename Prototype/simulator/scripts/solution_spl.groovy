package simulator
// CORRECT
// level and robotId are defined in binding
String goalStr = level.getGoal(robotId)
def goals = goalStr.split(" ")
int gx = goals[0] as int
int gy = goals[1] as int
String xStr = level.getSensorReadings(robotId, "x")
String yStr = level.getSensorReadings(robotId, "y")
def y = yStr as int
def x = xStr as int
if (x < gx)
    return "right " + (gx - x)
if (x > gx)
    return "left " + (x - gx)
if (y < gy)
    return "up " + (gy - y)
if (y > gy)
    return "down " + (y - gy)
return "stay 1"
