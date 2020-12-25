package bg.sofia.uni.fmi.mjt.dungeons.game.state;

public class Player implements Actor {

    private static final long serialVersionUID = 1L;

    private Position2D position;

    public Player(Position2D position) {
        this.position = position;
    }

    Position2D getPosition() {
        return this.position;
    }

    void setPosition(Position2D position) {
        this.position = position;
    }
}
