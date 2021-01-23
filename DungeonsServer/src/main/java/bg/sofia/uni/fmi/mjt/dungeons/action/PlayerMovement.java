package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;
import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType.MOVEMENT;

public class PlayerMovement extends AbstractPlayerAction {

    private Direction direction;

    public PlayerMovement(String clientCommand, SocketChannel initiator) {
        super(initiator);
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
    public ActionType type() {
        return MOVEMENT;
    }

    public Direction direction() {
        return direction;
    }
}
