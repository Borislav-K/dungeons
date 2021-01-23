package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;

public interface FightableActor extends Actor {
    int XPReward();

    BattleStats stats();

    int level();
}
