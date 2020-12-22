package bg.sofia.uni.fmi.mjt.dungeons.game.event;

public class Event {

    private String text;
    private int playerId;

    public Event(String text, int playerId) {
        this.text = text;
        this.playerId = playerId;
    }

    public String getText() {
        return text;
    }

    public int getInitiator() {
        return playerId;
    }

}
