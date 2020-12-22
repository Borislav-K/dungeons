package bg.sofia.uni.fmi.mjt.dungeons.game.event;

import java.util.concurrent.ConcurrentLinkedQueue;

// Events are meant to be consumed by a single thread
public class EventBus {

    ConcurrentLinkedQueue<Event> events;

    public EventBus() {
        this.events = new ConcurrentLinkedQueue<>();
    }

    public void publish(Event e) {
        events.add(e);
    }

    public Event getNext() {
        return events.poll();
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }


}
