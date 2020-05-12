package simulator.playback

class GameObjectStateProto{
    int startingFrame, endingFrame
    Vec3Proto position, rotation, dimension
    int color

    GameObjectStateProto(int startingFrame, int color, Vec3Proto position, Vec3Proto rotation, Vec3Proto dimension) {
        this.startingFrame = startingFrame
        this.position = position
        this.rotation = rotation
        this.dimension = dimension
        this.color = color
    }
}
