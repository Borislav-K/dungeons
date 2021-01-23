package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import javax.swing.JFrame;

public class GameWindow extends JFrame {

    public GameWindow(Renderer renderer) {
        this.setTitle("Dungeons");
        this.setSize(700, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.add(renderer);
    }

}
