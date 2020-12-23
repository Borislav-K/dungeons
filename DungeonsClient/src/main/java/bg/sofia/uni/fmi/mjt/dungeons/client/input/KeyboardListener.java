package bg.sofia.uni.fmi.mjt.dungeons.client.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardListener extends KeyAdapter {


    private KeyboardEventHandler keyboardEventHandler;

    public KeyboardListener(KeyboardEventHandler keyboardEventHandler) {
        this.keyboardEventHandler = keyboardEventHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.printf("Pressed %d\n", keyCode);
        keyboardEventHandler.publishCommand(keyCode);

    }

}
