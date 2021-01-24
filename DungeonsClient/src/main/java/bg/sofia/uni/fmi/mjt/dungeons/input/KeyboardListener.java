package bg.sofia.uni.fmi.mjt.dungeons.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyboardListener extends KeyAdapter {

    private static final String MOVE_LEFT_CMD = "mvl";
    private static final String MOVE_RIGHT_CMD = "mvr";
    private static final String MOVE_UP_CMD = "mvu";
    private static final String MOVE_DOWN_CMD = "mvd";
    private static final String FIGHT_CMD = "att";
    private static final String PICKUP_CMD = "pck";
    private static final String USE_ITEM_CMD_FORMAT = "us%d";
    private static final String GIVE_ITEM_CMD_FORMAT = "gv%d";
    private static final String THROW_ITEM_CMD_FORMAT = "th%d";

    private static final Map<Integer, String> keybinds = getKeybinds();
    private static final Map<Integer, String> keybindsWithShift = getShiftKeybinds();

    private KeyboardEventHandler keyboardEventHandler;

    public KeyboardListener(KeyboardEventHandler keyboardEventHandler) {
        this.keyboardEventHandler = keyboardEventHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (e.isShiftDown() && keybindsWithShift.containsKey(keyCode)) {
            keyboardEventHandler.publishCommand(keybindsWithShift.get(e.getKeyCode()));
            System.out.printf("Pressed shift+%d, will send %s to the server\n", keyCode, keybindsWithShift.get(keyCode));
        } else if (keybinds.containsKey(keyCode)) {
            keyboardEventHandler.publishCommand(keybinds.get(keyCode));
            System.out.printf("Pressed %d, will send %s to the server\n", keyCode, keybinds.get(keyCode));
        }

    }

    private static Map<Integer, String> getKeybinds() {
        Map<Integer, String> keybinds = new HashMap<>(Map.of(
                37, MOVE_LEFT_CMD, // Left arrow
                65, MOVE_LEFT_CMD, // A
                68, MOVE_RIGHT_CMD, // Right arrow
                39, MOVE_RIGHT_CMD, // D
                87, MOVE_UP_CMD, // Up arrow
                38, MOVE_UP_CMD, // W
                83, MOVE_DOWN_CMD, // Down arrow
                40, MOVE_DOWN_CMD, // S
                70, FIGHT_CMD, // F
                84, PICKUP_CMD)); // T
        for (int i = 1; i <= 9; i++) {
            keybinds.put(48 + i, USE_ITEM_CMD_FORMAT.formatted(i)); // 1-9
            keybinds.put(111 + i, GIVE_ITEM_CMD_FORMAT.formatted(i)); // F1-F9
        }
        return keybinds;
    }

    private static Map<Integer, String> getShiftKeybinds() {
        Map<Integer, String> shiftKeybinds = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            shiftKeybinds.put(48 + i, THROW_ITEM_CMD_FORMAT.formatted(i)); // shift+1, ..., shift+9
        }
        return shiftKeybinds;
    }
}
