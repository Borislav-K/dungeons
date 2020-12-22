package bg.sofia.uni.fmi.mjt.dungeons.game.event;

public class EventHandler {

    private EventBus eventBus;

    public EventHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handleEvents() {
        while (eventBus.hasEvents()) {
            handleEvent(eventBus.getNext());
        }
    }

    private void handleEvent(Event event) {
        System.out.printf("Handling %s for player #%d\n", event.getText(),event.getInitiator());
    }
}
