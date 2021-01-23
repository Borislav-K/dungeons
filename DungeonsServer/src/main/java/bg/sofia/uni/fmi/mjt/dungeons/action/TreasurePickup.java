package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType.TREASURE_PICKUP;

public class TreasurePickup implements PlayerAction {

    private SocketChannel channel;

    public TreasurePickup(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public ActionType type() {
        return TREASURE_PICKUP;
    }

    @Override
    public SocketChannel initiator() {
        return channel;
    }
}
