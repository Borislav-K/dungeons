package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.input.KeyboardEventHandler;
import bg.sofia.uni.fmi.mjt.dungeons.input.KeyboardListener;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.network.GameClient;
import bg.sofia.uni.fmi.mjt.dungeons.rendering.GameWindow;
import bg.sofia.uni.fmi.mjt.dungeons.rendering.Renderer;

import java.io.IOException;


public class Game {

    private static final double FRAME_NANOS = 17000000.0;

    private static final int FRAMES_AFTER_DEATH_THRESHOLD = 60 * 3;
    private static final int FRAMES_WITH_NO_RESPONSE_THRESHOLD = 60 * 10;

    private GameClient webClient;
    private Renderer renderer;
    private GameWindow gameWindow;
    private KeyboardEventHandler keyboardEventHandler;
    private KeyboardListener keyboardListener;

    private boolean shouldTerminate = false;
    private int consecutiveFramesWithoutResponse = 0;
    private int framesAfterPlayerDied = 0;

    public Game() {
        webClient = new GameClient();
        renderer = new Renderer();
        gameWindow = new GameWindow(renderer);
        keyboardEventHandler = new KeyboardEventHandler(webClient);
        keyboardListener = new KeyboardListener(keyboardEventHandler);
    }

    public void start() {
        connectToServer();
        gameWindow.addKeyListener(keyboardListener);
        startLoop();
    }

    private void connectToServer() {
        try {
            webClient.connect();
        } catch (IOException e) {
            System.out.println("Startup failed: could not connect to the game server.");
            e.printStackTrace();
            gameWindow.dispose(); // Will terminate the application
        }
    }

    private void startLoop() {
        double framesElapsed = 0;
        long lastMoment = System.nanoTime();
        while (!shouldTerminate) {
            if (framesAfterPlayerDied == FRAMES_AFTER_DEATH_THRESHOLD ||
                consecutiveFramesWithoutResponse == FRAMES_WITH_NO_RESPONSE_THRESHOLD) {
                terminate();
            }
            long now = System.nanoTime();
            framesElapsed += (now - lastMoment) / FRAME_NANOS;
            lastMoment = now;
            if (framesElapsed >= 1) {
                tick();
                framesElapsed--;
            }
        }
    }

    private void tick() {
        keyboardEventHandler.handleNext();
        PlayerSegment playerSegment = webClient.fetchStateFromServer();
        if (playerSegment == null) {
            consecutiveFramesWithoutResponse++;
            return;
        }
        consecutiveFramesWithoutResponse = 0;
        if (playerSegment.type().equals(PlayerSegmentType.DEATH)) {
            if (framesAfterPlayerDied == 0) {
                gameWindow.removeKeyListener(keyboardListener);
            }
            framesAfterPlayerDied++;
        }
        renderer.updatePlayerSegment(playerSegment);
        gameWindow.repaint();
    }

    private void terminate() {
        webClient.disconnect();
        gameWindow.dispose();
        shouldTerminate = true;
    }

}
