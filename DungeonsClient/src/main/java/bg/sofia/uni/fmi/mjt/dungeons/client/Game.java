package bg.sofia.uni.fmi.mjt.dungeons.client;

import bg.sofia.uni.fmi.mjt.dungeons.client.input.KeyboardEventHandler;
import bg.sofia.uni.fmi.mjt.dungeons.client.input.KeyboardListener;
import bg.sofia.uni.fmi.mjt.dungeons.client.network.GameClient;
import bg.sofia.uni.fmi.mjt.dungeons.client.rendering.GameWindow;
import bg.sofia.uni.fmi.mjt.dungeons.client.rendering.RenderableMap;

import java.io.IOException;


public class Game {

    private static final double FRAME_NANOS = 17000000.0;

    private GameClient webClient;
    private RenderableMap gameMap;
    private GameWindow gameWindow;
    private KeyboardEventHandler keyboardEventHandler;

    public Game() {
        webClient = new GameClient();
        gameMap = new RenderableMap();
        gameWindow = new GameWindow(gameMap);
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
        char[][] newMap = webClient.fetchMapFromServer();
        gameMap.updateMap(newMap);
        gameWindow.repaint();
    }


}
