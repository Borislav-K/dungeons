package bg.sofia.uni.fmi.mjt.dungeons.client.input;

import bg.sofia.uni.fmi.mjt.dungeons.client.network.GameClient;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class KeyboardEventHandler {

    private static final String MOVE_LEFT_CMD = "mvl";
    private static final String MOVE_RIGHT_CMD = "mvr";
    private static final String MOVE_UP_CMD = "mvu";
    private static final String MOVE_DOWN_CMD = "mvd";

    private static final Map<Integer, String> keybinds = Map.of(
            37, MOVE_LEFT_CMD,
            65, MOVE_LEFT_CMD,
            68, MOVE_RIGHT_CMD,
            39, MOVE_RIGHT_CMD,
            87, MOVE_UP_CMD,
            38, MOVE_UP_CMD,
            83, MOVE_DOWN_CMD,
            40, MOVE_DOWN_CMD
    );

    private Queue<Integer> commands; // Needed, otherwise 2 commands can be sent per frame, crashing the server


    private GameClient webClient;

    public KeyboardEventHandler(GameClient webClient) {
        this.commands = new LinkedList<>();
        this.webClient = webClient;
    }

    public void publishCommand(int keyCode) {
        commands.add(keyCode);
    }

    public void handleNext() {
        if (!commands.isEmpty()) {
            int nextCommandKeyCode = commands.poll();
            webClient.sendMessage(keybinds.get(nextCommandKeyCode));
        }
    }

}
