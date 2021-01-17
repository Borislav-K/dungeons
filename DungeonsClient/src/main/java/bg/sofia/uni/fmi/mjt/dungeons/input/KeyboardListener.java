package bg.sofia.uni.fmi.mjt.dungeons.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class KeyboardListener extends KeyAdapter {

    private static final String MOVE_LEFT_CMD = "mvl";
    private static final String MOVE_RIGHT_CMD = "mvr";
    private static final String MOVE_UP_CMD = "mvu";
    private static final String MOVE_DOWN_CMD = "mvd";
    private static final String FIGHT_CMD = "att";

    private static final Map<Integer, String> keybinds = Map.of(
            37, MOVE_LEFT_CMD,
            65, MOVE_LEFT_CMD,
            68, MOVE_RIGHT_CMD,
            39, MOVE_RIGHT_CMD,
            87, MOVE_UP_CMD,
            38, MOVE_UP_CMD,
            83, MOVE_DOWN_CMD,
            40, MOVE_DOWN_CMD,
            70, FIGHT_CMD
    );

    private KeyboardEventHandler keyboardEventHandler;

    public KeyboardListener(KeyboardEventHandler keyboardEventHandler) {
        this.keyboardEventHandler = keyboardEventHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        if (keybinds.containsKey(keyCode)) {
            keyboardEventHandler.publishCommand(keybinds.get(keyCode));
            System.out.printf("Pressed %d, will send %s to the server\n", keyCode, keybinds.get(keyCode));
        }

    }

}
