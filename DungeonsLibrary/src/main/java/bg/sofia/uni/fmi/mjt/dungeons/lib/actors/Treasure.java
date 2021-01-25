package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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
        this.position=position;
    }

    // Treasures' data does not need to be sent to the clients. Their only important characteristic is their actor type
    @Override
    public void serialize(DataOutputStream out) {
    }

    @Override
    public void deserialize(DataInputStream in) {
    }
}
