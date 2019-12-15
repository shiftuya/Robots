package lobbymanager

class LevelClass implements Level {
    String name
    LevelDescription description

    @Override
    String getName() {
        return name
    }

    @Override
    LevelDescription getDescription() {
        return description
    }
}
