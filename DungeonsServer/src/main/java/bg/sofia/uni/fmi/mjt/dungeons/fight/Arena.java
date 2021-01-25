package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.FightableActor;

// Implements Player vs. Player and Player vs. Minion combat
public class Arena {

    // The initiator is always a player
    public static FightResult makeActorsFight(FightableActor initiator, FightableActor subject) {
        while (true) {
            initiator.dealDamage(subject);
            if (subject.currentHealth() == 0) {
                return new FightResult(initiator, subject);
            }
            subject.dealDamage(initiator);
            if (initiator.currentHealth() == 0) {
                return new FightResult(subject, initiator);
            }
        }
    }

}
