package bg.sofia.uni.fmi.mjt.dungeons.input;

import bg.sofia.uni.fmi.mjt.dungeons.network.GameClient;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class KeyboardEventHandler {

    private static final int MAX_COMMANDS_PER_FRAME = 3;

    private Queue<String> commands;
    private GameClient webClient;

    public KeyboardEventHandler(GameClient webClient) {
        this.commands = new ArrayBlockingQueue<>(MAX_COMMANDS_PER_FRAME);
        this.webClient = webClient;
    }

    public void publishCommand(String command) {
        if (commands.size() == MAX_COMMANDS_PER_FRAME) {
            return;
        }
        commands.add(command);
    }

    public void handleNext() {
        if (!commands.isEmpty()) {
            String nextCommand = commands.poll();
            webClient.sendMessage(nextCommand);
        }
    }

}
