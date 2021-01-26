package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;


public class ItemThrow extends PlayerAction {

    private int itemNumber;

    public ItemThrow(SocketChannel initiator, int itemNumber) {
        super(initiator);
        this.itemNumber = itemNumber;
    }

    @Override
    public ActionType type() {
        return ActionType.ITEM_THROW;
    }

    public int itemNumber() {
        return itemNumber;
    }
}
