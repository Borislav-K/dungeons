package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.FightableActor;

public record FightResult(FightableActor winner, FightableActor loser) {

}
