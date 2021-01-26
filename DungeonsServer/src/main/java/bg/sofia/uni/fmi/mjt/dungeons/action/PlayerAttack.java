package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerAttack extends PlayerAction {

    public PlayerAttack(SocketChannel initiator) {
        super(initiator);
    }

    @Override
    public ActionType type() {
        return ActionType.ATTACK;
    }
}
