package bg.sofia.uni.fmi.mjt.dungeons.actors;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.game.Position2D;

public interface Actor {
    ActorType type();

    int XPReward();

    Position2D position();
}
