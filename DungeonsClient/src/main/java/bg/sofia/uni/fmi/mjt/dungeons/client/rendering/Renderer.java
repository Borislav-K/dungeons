package bg.sofia.uni.fmi.mjt.dungeons.client.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.game.state.PlayerSegment;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    private static final int MAP_FIELD_SIZE = 25;

    private static final int XP_TEXT_LOCATION_X = 220;
    private static final int XP_TEXT_LOCATION_Y = 550;
    private static final int XP_BAR_UPPER_CORNER_X = 20;
    private static final int XP_BAR_UPPER_CORNER_Y = 525;
    private static final int XP_BAR_WIDTH = 450;
    private static final int XP_BAR_HEIGHT = 30;
    private static final int LEVEL_LABEL_LOCATION_X = 195;
    private static final int LEVEL_LABEL_LOCATION_Y = 580;

    private PlayerSegment currentSegment;


    public Renderer() {
        this.currentSegment = new PlayerSegment();
    }

    public void updateState(PlayerSegment newSegment) {
        this.currentSegment = newSegment;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        paintMap(g2d);
        paintXPBar(g2d);
    }

    private void paintMap(Graphics2D g2d) {
        var mapFields = currentSegment.GameMap().getFields();
        g2d.setStroke(new BasicStroke(5));

        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE));
        for (int i = 0; i < mapFields.length; i++) {
            for (int j = 0; j < mapFields[i].length; j++) {
                g2d.drawString(String.valueOf(mapFields[i][j]), MAP_FIELD_SIZE * i, MAP_FIELD_SIZE * (j + 1));
            }
        }
    }

    private void paintXPBar(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLUE);
        g2d.drawRect(XP_BAR_UPPER_CORNER_X, XP_BAR_UPPER_CORNER_Y, XP_BAR_WIDTH, XP_BAR_HEIGHT);

        String levelLabel = "Level ".concat(String.valueOf(currentSegment.playerData().currentLevel()));
        String currentXPPercentage = String.valueOf(currentSegment.playerData().experiencePercentage());

        g2d.drawString(currentXPPercentage, XP_TEXT_LOCATION_X, XP_TEXT_LOCATION_Y);
        g2d.drawString(levelLabel, LEVEL_LABEL_LOCATION_X, LEVEL_LABEL_LOCATION_Y);

    }
}
