package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.input.KeyboardEventHandler;
import bg.sofia.uni.fmi.mjt.dungeons.input.KeyboardListener;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.DefaultPlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.network.GameClient;
import bg.sofia.uni.fmi.mjt.dungeons.rendering.GameWindow;
import bg.sofia.uni.fmi.mjt.dungeons.rendering.Renderer;

import java.io.IOException;


public class Game {

    private static final double FRAME_NANOS = 17000000.0;

    private GameClient webClient;
    private Renderer renderer;
    private GameWindow gameWindow;
    private KeyboardEventHandler keyboardEventHandler;

    public Game() {
        webClient = new GameClient();
        renderer = new Renderer();
        gameWindow = new GameWindow(renderer);
        keyboardEventHandler = new KeyboardEventHandler(webClient);
    }

    public void start() {
        connectToServer();
        addKeyboardListener();
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

    private void addKeyboardListener() {
        KeyboardListener k = new KeyboardListener(keyboardEventHandler);
        gameWindow.addKeyListener(k);
    }

    private void startLoop() {
        double framesElapsed = 0;
        long lastMoment = System.nanoTime();
        while (true) {
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

        if (playerSegment != null) {
            renderer.renderNewState(playerSegment);
            gameWindow.repaint();
        }
    }

}
