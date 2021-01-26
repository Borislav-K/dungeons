package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerConnect extends PlayerAction {

    public PlayerConnect(SocketChannel initiator) {
        super(initiator);
    }

    public ActionType type() {
        return ActionType.PLAYER_CONNECT;
    }
}
