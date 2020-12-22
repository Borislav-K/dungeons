package bg.sofia.uni.fmi.mjt.dungeons.client;

import bg.sofia.uni.fmi.mjt.dungeons.client.input.KeyboardEventHandler;
import bg.sofia.uni.fmi.mjt.dungeons.client.input.KeyboardListener;
import bg.sofia.uni.fmi.mjt.dungeons.client.rendering.GameWindow;

import java.io.IOException;

public class ClientLauncher {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10_000;

    public static void main(String[] args) {
        GameClient webClient = new GameClient(SERVER_HOST, SERVER_PORT);
        GameWindow gameWindow = new GameWindow(); // Immediately displays the frame
        try {
            webClient.start();
        } catch (IOException e) {
            System.out.println("Startup failed: could not connect to the game server.");
            e.printStackTrace();
            gameWindow.dispose(); // Will terminate the application
        }
        KeyboardEventHandler keyboardEventHandler = new KeyboardEventHandler(webClient);
        KeyboardListener k = new KeyboardListener(keyboardEventHandler);
        gameWindow.addKeyListener(k);
    }
}
