package bg.sofia.uni.fmi.mjt.dungeons.client.input;

import bg.sofia.uni.fmi.mjt.dungeons.client.network.GameClient;

import java.util.Map;

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

    private GameClient webClient;

    public KeyboardEventHandler(GameClient webClient) {
        this.webClient = webClient;
    }

    public void handleKeyboardEvent(int keyCode) { // TODO check handling of multiple movement keys pressed at once
        if (keybinds.containsKey(keyCode)) {
            webClient.sendMessage(keybinds.get(keyCode));
        }
    }

}
