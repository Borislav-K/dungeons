package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class ItemUsage extends PlayerAction {

    private int itemNumber;

    public ItemUsage(SocketChannel initiator, int itemNumber) {
        super(initiator);
        this.itemNumber = itemNumber;
    }

    @Override
    public ActionType type() {
        return ActionType.ITEM_USAGE;
    }

    public int itemNumber() {
        return itemNumber;
    }

}
