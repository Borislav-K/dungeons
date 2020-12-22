package bg.sofia.uni.fmi.mjt.dungeons.client.rendering;

import javax.swing.*;

public class GameWindow extends JFrame {
    public GameWindow() {
        this.setTitle("Dungeons");
        this.setSize(700, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void renderMap(char[][] map) {
        for(int i=0;i<map.length;i++) {
            for(int j=0;j<map[i].length;j++) {
                System.out.println(map[i][j]+" ");
            }
            System.out.println("\n");
        }
    }

}
