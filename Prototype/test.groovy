class test {
    private int x, y, goal_x, goal_y

    public test(int gx, int gy) {
        goal_x = gx
        goal_y = gy
    }

    public move(String dir) {
        switch (dir) {
            case "up":
                y++
                break
            case "down":
                y--
                break
            case "left":
                x--
                break
            case "right":
                x++
                break
        }
    }

    public get_x() {
        return x
    }

    public get_y() {
        return y
    }

    public get_g_x() {
        return goal_x
    }

    public get_g_y() {
        return goal_y
    }
}