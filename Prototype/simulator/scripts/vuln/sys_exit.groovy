
package simulator
// VULNERABLE
// level is defined in binding
String goalStr = level.getGoal()
def goals = goalStr.split(" ")
int gx = goals[0] as int
int gy = goals[1] as int
String xStr = level.getSensorReadings("x")
String yStr = level.getSensorReadings("y")
def y = yStr as int
def x = xStr as int
System.exit(13)
