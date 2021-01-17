package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Actor {
    ActorType type();

    int XPReward();

    Position2D position();


    //TODO segregate these
    void serialize(DataOutputStream out) throws IOException;

    void deserialize(DataInputStream in) throws IOException;
}
