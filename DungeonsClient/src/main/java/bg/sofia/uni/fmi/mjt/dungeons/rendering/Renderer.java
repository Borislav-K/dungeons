package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.DefaultPlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;

import javax.swing.*;
import java.awt.*;

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
        } else if (lastReceivedSegment.type().equals(PlayerSegmentType.DEATH)) {
            renderDeathMessage(g2d);
        } else {
            DefaultPlayerSegment defaultPlayerSegment = (DefaultPlayerSegment) lastReceivedSegment;
            Player currentPlayer = defaultPlayerSegment.player();

            MapRenderer.renderMap(g2d, defaultPlayerSegment.positionsWithActors());
            PlayerDataRenderer.renderPlayerStats(g2d, currentPlayer);
            InventoryRenderer.renderInventory(g2d, currentPlayer.inventory());
        }
    }

    private void renderNotConnectedMessage(Graphics2D g2d) {
        g2d.drawString("Not connected to the server yet...", 250, 250);
    }

    private void renderDeathMessage(Graphics2D g2d) {
        g2d.drawString("YOU DIED", 250, 250);
    }

}

