package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.IllegalPlayerActionException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Treasure;

import java.nio.channels.SocketChannel;

public interface PlayerAction {

    String MOVE_UP_CMD = "mvu";
    String MOVE_DOWN_CMD = "mvd";
    String MOVE_LEFT_CMD = "mvl";
    String MOVE_RIGHT_CMD = "mvr";
    String ATTACK_CMD = "att";
    String TREASURE_PICKUP_CMD = "pck";

    ActionType type();

    SocketChannel initiator();

    static PlayerAction of(String clientCommand, SocketChannel initiator) throws IllegalPlayerActionException {
        return switch (clientCommand) {
            case MOVE_DOWN_CMD, MOVE_LEFT_CMD, MOVE_RIGHT_CMD, MOVE_UP_CMD -> new PlayerMovement(clientCommand, initiator);
            case ATTACK_CMD -> new PlayerAttack(initiator);
            case TREASURE_PICKUP_CMD -> new TreasurePickup(initiator);
            default -> throw new IllegalPlayerActionException(String.format("%s is not a valid command", clientCommand));
        };
    }
}
