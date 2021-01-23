package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class ItemGrant extends AbstractPlayerAction {

    private int itemNumber;

    protected ItemGrant(SocketChannel initiator, int itemNumber) {
        super(initiator);
        this.itemNumber = itemNumber;
    }

    @Override
    public ActionType type() {
        return ActionType.ITEM_GRANT;
    }

    public int itemNumber() {
        return itemNumber;
    }
}
