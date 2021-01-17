package bg.sofia.uni.fmi.mjt.dungeons.input;

import bg.sofia.uni.fmi.mjt.dungeons.network.GameClient;

import java.util.LinkedList;
import java.util.Queue;

public class KeyboardEventHandler {

    private Queue<String> commands; // Needed, otherwise 2 commands can be sent per frame, crashing the server


    private GameClient webClient;

    public KeyboardEventHandler(GameClient webClient) {
        this.commands = new LinkedList<>();
        this.webClient = webClient;
    }

    public void publishCommand(String command) {
        commands.add(command);
    }

    public void handleNext() {
        if (!commands.isEmpty()) {
            String nextCommand = commands.poll();
            webClient.sendMessage(nextCommand);
        }
    }

}
