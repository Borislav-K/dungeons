package bg.sofia.uni.fmi.mjt.dungeons.game.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerConnect implements PlayerAction {

    private SocketChannel channel;

    public PlayerConnect(SocketChannel channel) {
        this.channel = channel;
    }

    public ActionType getType() {
        return ActionType.PLAYER_CONNECT;
    }

    public SocketChannel getChannel() {
        return channel;
    }
}
