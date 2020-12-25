package bg.sofia.uni.fmi.mjt.dungeons.game.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerDisconnect implements PlayerAction {

    private SocketChannel channel;

    public PlayerDisconnect(SocketChannel channel) {
        this.channel = channel;
    }

    public ActionType getType() {
        return ActionType.PLAYER_DISCONNECT;
    }

    public SocketChannel getChannel() {
        return channel;
    }
}