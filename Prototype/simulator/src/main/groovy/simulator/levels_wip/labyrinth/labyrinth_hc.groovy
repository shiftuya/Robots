package simulator.levels_wip.labyrinth

public class Memory {
    int turn
}

def cheat = ["up 1", "right 4", "down 2", "right 1", "sleep 1"]

if (memory.class.name != "Memory") {
    memory = new Memory()
    memory.turn = 0
    level.writeLog("INITIALIZING")
}
memory.turn++
return cheat[memory.turn - 1]
