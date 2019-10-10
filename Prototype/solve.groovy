def goal_x = level.get_g_x()
def goal_y = level.get_g_y()
while (level.get_x() > goal_x) {
    level.move("left")
}
while (level.get_x() < goal_x) {
    level.move("right")
}
while (level.get_y() > goal_y) {
    level.move("down")
}
while (level.get_y() < goal_y) {
    level.move("up")
}