package simulator
// WRONG
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
    return "right 150"
if (x > gx)
    return "left 150"
if (y < gy)
    return "up 150"
if (y > gy)
    return "down 150"
return "stay 100"
