package bg.sofia.uni.fmi.mjt.dungeons.game.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;
import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType.MOVEMENT;

public class PlayerMovement implements PlayerAction {

    private Direction direction;
    private SocketChannel initiator;

    public PlayerMovement(String clientCommand, SocketChannel initiator) {
        this.initiator = initiator;
        this.direction = determineDirection(clientCommand);
    }

    private static Direction determineDirection(String command) {
        return switch (command) {
            case MOVE_UP_CMD -> Direction.UP;
            case MOVE_DOWN_CMD -> Direction.DOWN;
            case MOVE_LEFT_CMD -> Direction.LEFT;
            case MOVE_RIGHT_CMD -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Unknown movement command");
        };
    }

    @Override
    public ActionType getType() {
        return MOVEMENT;
    }

    public Direction getDirection() {
        return direction;
    }

    public SocketChannel getInitiator() {
        return this.initiator;
    }
}
