package bg.sofia.uni.fmi.mjt.dungeons.client.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.game.state.GameState;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    private static final int MAP_FIELD_SIZE = 25;

    private GameState gameState;


    public Renderer() {
        this.gameState = new GameState();
    }

    public void updateState(GameState newState) {
        this.gameState = newState;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        paintMap(g2d);
    }

    private void paintMap(Graphics2D g2d) {
        var mapFields = gameState.GameMap().getFields();
        g2d.setStroke(new BasicStroke(5));
        // TODO see drawBytes
        //Increasing Y -> Moving downwards
        //Increasing X -> Moving to the right

        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.ITALIC, MAP_FIELD_SIZE));
        for (int i = 0; i < mapFields.length; i++) {
            for (int j = 0; j < mapFields[i].length; j++) {
                g2d.drawString(String.valueOf(mapFields[i][j]), MAP_FIELD_SIZE * j, MAP_FIELD_SIZE * (i + 1));
            }
        }
    }

}
