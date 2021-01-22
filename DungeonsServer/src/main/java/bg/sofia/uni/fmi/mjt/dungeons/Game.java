package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.action.PlayerActionHandler;
import bg.sofia.uni.fmi.mjt.dungeons.network.GameServer;
import bg.sofia.uni.fmi.mjt.dungeons.network.StateDistributor;

public class Game {
    private static final double FRAME_NANOS = 17000000.0;

    private PlayerManager playerManager;
    private GameMap gameMap;
    private PlayerActionHandler playerActionHandler;
    private GameServer server;
    private StateDistributor stateDistributor;

    public Game() {
        this.playerManager = new PlayerManager();
        this.gameMap = new GameMap();
        this.playerActionHandler = new PlayerActionHandler(playerManager, gameMap);
        this.server = new GameServer(playerActionHandler);
        this.stateDistributor = new StateDistributor(playerManager, gameMap);
    }

    public void start() {
        server.start();
        startLoop();
    }

    private void startLoop() {
        double framesElapsed = 0;
        long lastMoment = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            framesElapsed += (now - lastMoment) / FRAME_NANOS;
            lastMoment = now;
            while (framesElapsed >= 1) { // Catch up with the frames if there was a delay
                tick();
                framesElapsed--;
            }
        }
    }

    private void tick() {
        server.fetchPlayerActions();
        playerActionHandler.handleAll();
        stateDistributor.distributeState();
    }
}
