package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treasure treasure = (Treasure) o;
        return Objects.equals(type(), treasure.type());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ActorType.TREASURE.ordinal());
    }
}
