package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.IllegalPlayerActionException;

import java.nio.channels.SocketChannel;

public class PlayerActionFactory {
    private static final String MOVE_UP_CMD = "mvu";
    private static final String MOVE_DOWN_CMD = "mvd";
    private static final String MOVE_LEFT_CMD = "mvl";
    private static final String MOVE_RIGHT_CMD = "mvr";
    private static final String ATTACK_CMD = "att";
    private static final String TREASURE_PICKUP_CMD = "pck";
    private static final String ITEM_USAGE_REGEX = "us[0-9]";
    private static final String ITEM_GRANT_REGEX = "gv[0-9]";
    private static final String ITEM_THROW_REGEX = "th[0-9]";

    public static PlayerAction create(String command, SocketChannel initiator) throws IllegalPlayerActionException {
        if (command.matches(ITEM_USAGE_REGEX)) {
            return new ItemUsage(initiator, command.charAt(2) - '0');
        }
        if (command.matches(ITEM_GRANT_REGEX)) {
            return new ItemGrant(initiator, command.charAt(2) - '0');
        }
        if (command.matches(ITEM_THROW_REGEX)) {
            return new ItemThrow(initiator, command.charAt(2) - '0');
        }
        return switch (command) {
            case MOVE_DOWN_CMD, MOVE_LEFT_CMD, MOVE_RIGHT_CMD, MOVE_UP_CMD -> createPlayerMovement(initiator, command);
            case ATTACK_CMD -> new PlayerAttack(initiator);
            case TREASURE_PICKUP_CMD -> new TreasurePickup(initiator);
            default -> throw new IllegalPlayerActionException(String.format("%s is not a valid command", command));
        };
    }

    private static PlayerMovement createPlayerMovement(SocketChannel initiator, String command) {
        Direction direction = switch (command) {
            case MOVE_UP_CMD -> Direction.UP;
            case MOVE_DOWN_CMD -> Direction.DOWN;
            case MOVE_LEFT_CMD -> Direction.LEFT;
            case MOVE_RIGHT_CMD -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Unknown movement command");
        };
        return new PlayerMovement(initiator, direction);
    }
}
