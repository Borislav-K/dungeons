package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.IllegalPlayerActionException;

import java.nio.channels.SocketChannel;

public interface PlayerAction {

    String MOVE_UP_CMD = "mvu";
    String MOVE_DOWN_CMD = "mvd";
    String MOVE_LEFT_CMD = "mvl";
    String MOVE_RIGHT_CMD = "mvr";
    String ATTACK_CMD = "att";
    String TREASURE_PICKUP_CMD = "pck";
    String ITEM_USAGE_REGEX = "us[0-9]";
    String ITEM_GRANT_REGEX = "gv[0-9]";

    ActionType type();

    SocketChannel initiator();

    static PlayerAction of(String clientCommand, SocketChannel initiator) throws IllegalPlayerActionException {
        if (clientCommand.matches(ITEM_USAGE_REGEX)) {
            return new ItemUsage(initiator, clientCommand.charAt(2) - '0');
        }
        if (clientCommand.matches(ITEM_GRANT_REGEX)) {
            return new ItemGrant(initiator, clientCommand.charAt(2) - '0');
        }
        return switch (clientCommand) {
            case MOVE_DOWN_CMD, MOVE_LEFT_CMD, MOVE_RIGHT_CMD, MOVE_UP_CMD -> new PlayerMovement(clientCommand, initiator);
            case ATTACK_CMD -> new PlayerAttack(initiator);
            case TREASURE_PICKUP_CMD -> new TreasurePickup(initiator);
            default -> throw new IllegalPlayerActionException(String.format("%s is not a valid command", clientCommand));
        };
    }
}
