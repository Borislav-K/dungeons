package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class Renderer extends JPanel {

    private PlayerSegment lastReceivedSegment;

    public Renderer() {
        super.setBackground(Color.white);
    }

    public void updatePlayerSegment(PlayerSegment newSegment) {
        this.lastReceivedSegment = newSegment;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (lastReceivedSegment == null) {
            renderNotConnectedMessage(g2d);
        } else {
            Player currentPlayer = lastReceivedSegment.player();

            MapRenderer.renderMap(g2d, lastReceivedSegment.positionsWithActors());
            PlayerDataRenderer.renderPlayerStats(g2d, currentPlayer);
            InventoryRenderer.renderInventory(g2d, currentPlayer.inventory());
        }
    }

    private void renderNotConnectedMessage(Graphics2D g2d) {
        g2d.drawString("Not connected to the server yet...", 250, 250);
    }

}

