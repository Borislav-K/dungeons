package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public abstract class PlayerAction {

    protected SocketChannel initiator;

    protected PlayerAction(SocketChannel initiator) {
        this.initiator = initiator;
    }

    public SocketChannel initiator() {
        return initiator;
    }

    public abstract ActionType type();

}
