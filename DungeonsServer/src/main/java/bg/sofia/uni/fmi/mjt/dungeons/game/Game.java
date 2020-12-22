package bg.sofia.uni.fmi.mjt.dungeons.game;

import bg.sofia.uni.fmi.mjt.dungeons.game.event.EventBus;
import bg.sofia.uni.fmi.mjt.dungeons.game.event.EventHandler;
import bg.sofia.uni.fmi.mjt.dungeons.server.GameServer;


public class Game {
    private EventBus eventBus;
    private EventHandler eventHandler;
    private GameLoop loop;
    private GameServer server;

    public Game() {
        this.eventBus = new EventBus();
        this.eventHandler = new EventHandler(eventBus);
        this.loop = new GameLoop(eventBus, eventHandler);
        this.server = new GameServer(eventBus);
    }

    public void start() {
        server.start(); // Starts in a separate thread
        loop.start();
    }

    public void stopGame() {
        loop.terminate();
        server.shutdown();
    }


}
