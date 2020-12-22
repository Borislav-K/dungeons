package bg.sofia.uni.fmi.mjt.dungeons.game.event;

public class MovementEvent extends Event {

    public MovementEvent(String clientCommand, int playerId) {
        super(clientCommand, playerId);
    }

}
