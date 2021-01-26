package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;
import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType.MOVEMENT;

public class PlayerMovement extends PlayerAction {

    private Direction direction;

    public PlayerMovement(SocketChannel initiator, Direction direction) {
        super(initiator);
        this.direction = direction;
    }

    @Override
    public ActionType type() {
        return MOVEMENT;
    }

    public Direction direction() {
        return direction;
    }
}
