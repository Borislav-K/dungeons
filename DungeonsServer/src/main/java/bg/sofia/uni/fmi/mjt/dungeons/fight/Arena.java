package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.FightableActor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Weapon;

// Implements Player vs. Player and Player vs. Minion combat
public class Arena {

    // The initiator is always a player
    public static FightResult makeActorsFight(FightableActor initiator, FightableActor subject) {
        BattleStats initiatorStats = initiator.stats();
        BattleStats subjectStats = subject.stats();

        int initiatorDamage = initiatorStats.attack();
        int subjectDamage = subjectStats.attack();
        if (initiator.type().equals(ActorType.PLAYER)) {
            Weapon initiatorWeapon = ((Player) initiator).weapon();
            initiatorDamage += initiatorWeapon == null ? 0 : initiatorWeapon.attack();
        }
        if (subject.type().equals(ActorType.PLAYER)) {
            Weapon subjectWeapon = ((Player) subject).weapon();
            subjectDamage += subjectWeapon == null ? 0 : subjectWeapon.attack();
        }

        while (true) {
            subjectStats.takeDamage(initiatorDamage);
            if (subjectStats.currentHealth() == 0) {
                return new FightResult(initiator, subject);
            }
            if (initiatorStats.currentHealth() == 0) {
                return new FightResult(subject, initiator);
            }
            initiatorStats.takeDamage(subjectDamage);
        }
    }

}
