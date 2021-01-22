package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;

// Implements Player vs. Player and Player vs. Minion combat
public class Arena {

    // The initiator is always a player
    public static FightResult makeActorsFight(Actor initiator, Actor subject) {
        BattleStats initiatorStats = initiator.stats();
        BattleStats subjectStats = subject.stats();
        System.out.printf("Fight start: actor1 stats: %s, actor2 stats: %s.\n", initiatorStats.toString(),
                subjectStats.toString());
        while (subjectStats.currentHealth() > 0 && initiatorStats.currentHealth() > 0) {
            subjectStats.takeDamage(initiatorStats.attack());
            initiatorStats.takeDamage(subjectStats.attack());
        }
        if(initiatorStats.currentHealth()>0) {
            System.out.println("Winner is actor1");
        } else {
            System.out.println("Winner is actor2");
        }
        return initiatorStats.currentHealth() > 0 ?
                new FightResult(initiator, subject) :
                new FightResult(subject, initiator);
    }

}
