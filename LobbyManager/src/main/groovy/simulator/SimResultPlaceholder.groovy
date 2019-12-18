package simulator

import lobbymanager.SimulationLog
import lobbymanager.SimulationPlayback

class SimResultPlaceholder implements lobbymanager.SimulationResult {
    Date time
    boolean passed

    public SimResultPlaceholder(Date _time, boolean success) {
        time = _time
        passed = success
    }

    @Override
    Date getDate() {
        return time
    }

    @Override
    boolean isSuccessful() {
        return passed
    }

    @Override
    SimulationPlayback getPlayback() {
        return null
    }

    @Override
    SimulationLog getLog() {
        return null
    }
}
