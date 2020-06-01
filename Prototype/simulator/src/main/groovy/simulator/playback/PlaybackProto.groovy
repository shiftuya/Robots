package simulator.playback

class PlaybackProto implements Playback {
    int length
    List<List<GameObjectStateProto>> gameObjectStates
    Map<String, Integer> robots

    PlaybackProto(int length, List<List<GameObjectStateProto>> gameObjectStates) {
        this.length = length
        this.gameObjectStates = gameObjectStates
        for (List<GameObjectStateProto> states : gameObjectStates) {
            states.last().setEndingFrame(length)
        }
    }

    @Override
    int getFramesCount() {
        return length
    }

    @Override
    Map<String, Integer> getRobots() {
        return null
    }

    @Override
    List<List<GameObjectStateProto>> getGameObjectStates() {
        return gameObjectStates
    }
}
