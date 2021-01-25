package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

public class Treasure implements Actor {

    private Position2D position;

    public Treasure() {
    }

    public Treasure(Position2D position) {
        this.position = position;
    }

    @Override
    public ActorType type() {
        return ActorType.TREASURE;
    }

    @Override
    public Position2D position() {
        return position;
    }

    @Override
    public void setPosition(Position2D position) {
        this.position = position;
    }

}
