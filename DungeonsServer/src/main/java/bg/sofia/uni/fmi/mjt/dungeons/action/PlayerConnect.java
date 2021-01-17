package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerConnect implements PlayerAction {

    private SocketChannel initiator;

    public PlayerConnect(SocketChannel initiator) {
        this.initiator = initiator;
    }

    public ActionType type() {
        return ActionType.PLAYER_CONNECT;
    }

    @Override
    public SocketChannel initiator() {
        return initiator;
    }
}
