package bg.sofia.uni.fmi.mjt.dungeons.game.event;

import java.util.Set;

public class EventFactory {

    private static final Set<String> movementCommands = Set.of("mvl", "mvr", "mvd", "mvu");


    public static Event create(String clientCommand, int playerId) {
        if (movementCommands.contains(clientCommand)) {
            return new MovementEvent(clientCommand, playerId);
        }
        return null;
    }


}
