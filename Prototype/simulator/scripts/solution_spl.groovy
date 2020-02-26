package simulator
// level and robotId are defined in binding
String goalStr = level.getGoal(robotId)
def goals = goalStr.split()
int gx = goals[0].toInteger()
int gy = goals[1].toInteger()
String xStr = level.getSensorReadings(robotId, "x")
String yStr = level.getSensorReadings(robotId, "y")
def y = yStr.toInteger()
def x = xStr.toInteger()
if (x < gx)
    return "right " + (gx - x)
if (x > gx)
    return "left " + (x - gx)
if (y < gy)
    return "up " + (gy - y)
if (y > gy)
    return "down " + (y - gy)
return "stay 1"
