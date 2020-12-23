package bg.sofia.uni.fmi.mjt.dungeons.client.rendering;

import javax.swing.*;
import java.awt.*;

public class RenderableMap extends JPanel {

    public static final int MAP_DIMENSIONS = 10;
    private static final int FIELD_SIZE = 25;
    private char[][] map;

    public RenderableMap() {
        this.map = new char[MAP_DIMENSIONS][MAP_DIMENSIONS];
    }

    public void updateMap(char[][] map) {
        if (map != null) {
            this.map = map; //TODO if it's null X times, disconnect
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        // TODO see drawBytes
        //Increasing Y -> Moving downwards
        //Increasing X -> Moving to the right

        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.ITALIC, FIELD_SIZE));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                g2d.drawString(String.valueOf(map[i][j]), FIELD_SIZE * j, FIELD_SIZE * (i + 1));
            }
        }
    }

    private void drawLine(Graphics2D g2d) {
        g2d.setColor(Color.blue);
        g2d.drawLine(0, 0, 400, 400);
    }

    private void drawPolyLine(Graphics2D g2d) {
        int[] xPoints = {50, 100, 150, 200, 250, 300, 350};
        int[] yPoints = {350, 250, 275, 200, 275, 150, 100};
        int nPoints = xPoints.length;

        g2d.setColor(Color.GREEN);
        g2d.drawPolyline(xPoints, yPoints, nPoints);
    }

    private void drawString(Graphics2D g2d) {
        g2d.setFont(new Font("Comic Sans", Font.ITALIC, 25));
        g2d.setColor(Color.CYAN);
        g2d.drawString("test", 100, 100);
    }

    private void drawTriangle(Graphics2D g2d) {
        int[] xPoints = {100, 200, 300};
        int[] yPoints = {300, 127, 300};
        g2d.setColor(Color.yellow);
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawFilledTriangle(Graphics2D g2d) {
        int[] xPoints = {100, 200, 300};
        int[] yPoints = {300, 127, 300};
        g2d.setColor(Color.yellow);
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
}
