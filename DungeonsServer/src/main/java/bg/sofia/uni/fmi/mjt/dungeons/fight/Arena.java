package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.FightableActor;

// Implements Player vs. Player and Player vs. Minion combat
public class Arena {

    // The initiator is always a player
    public static FightResult makeActorsFight(FightableActor initiator, FightableActor subject) {
        BattleStats initiatorStats = initiator.stats();
        BattleStats subjectStats = subject.stats();
        while (true) {
            subjectStats.takeDamage(initiatorStats.attack());
            if (subjectStats.currentHealth() == 0) {
                return new FightResult(initiator, subject);
            }
            if (initiatorStats.currentHealth() == 0) {
                return new FightResult(subject, initiator);
            }
            initiatorStats.takeDamage(subjectStats.attack());
        }
    }

}
