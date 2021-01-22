package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

public interface Actor extends Transmissible {
    ActorType type();

    int XPReward();

    Position2D position();
}
