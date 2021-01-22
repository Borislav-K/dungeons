package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;

// Implements Player vs. Player and Player vs. Minion combat
public class Arena {

    // The initiator is always a player
    public static FightResult makeActorsFight(Actor initiator, Actor subject) {
        if (subject.type().equals(ActorType.PLAYER)) {
            return playerVsPlayer((Player) initiator, (Player) subject);
        } else if (subject.type().equals(ActorType.MINION)) {
            return playerVsMinion((Player) initiator, (Minion) subject);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static FightResult playerVsPlayer(Player player1, Player player2) {
        throw new UnsupportedOperationException();
    }

    private static FightResult playerVsMinion(Player player, Minion minion) {
        return new FightResult(player, minion);
    }
}
