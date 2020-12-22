package bg.sofia.uni.fmi.mjt.dungeons.game.event;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ActionQueue {

    ConcurrentLinkedQueue<PlayerAction> playerActions;

    public ActionQueue() {
        this.playerActions = new ConcurrentLinkedQueue<>();
    }

    public void publish(PlayerAction e) {
        playerActions.add(e);
    }

    public PlayerAction getNext() {
        return playerActions.poll();
    }

    public boolean hasEvents() {
        return !playerActions.isEmpty();
    }

}
