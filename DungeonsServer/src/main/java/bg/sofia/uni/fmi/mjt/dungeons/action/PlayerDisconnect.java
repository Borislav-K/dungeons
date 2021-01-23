package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerDisconnect extends AbstractPlayerAction {
    public PlayerDisconnect(SocketChannel channel) {
        super(channel);
    }

    public ActionType type() {
        return ActionType.PLAYER_DISCONNECT;
    }

}
