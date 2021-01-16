package bg.sofia.uni.fmi.mjt.dungeons.game.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

public class PlayerAttack implements PlayerAction {

    private SocketChannel initiator;

    public PlayerAttack(SocketChannel initiator) {
        this.initiator = initiator;
    }

    @Override
    public ActionType type() {
        return ActionType.ATTACK;
    }

    @Override
    public SocketChannel initiator() {
        return initiator;
    }
}
