package bg.sofia.uni.fmi.mjt.dungeons.client.rendering;

import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow(RenderableMap map) {
        this.setTitle("Dungeons");
        this.setSize(700, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.add(map);
    }

}
