package bg.sofia.uni.fmi.mjt.dungeons.client.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.game.state.PlayerSegment;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Renderer extends JPanel {

    /**
     * <h1> MAP SERIALIZATION FORMAT </h1>
     * The game map is a 20x20 array of bytes. Each byte represents the actors on the position [i][j]
     * There can be at most 2 actors on the same position - 2 players, player+minion, and player+treasure
     * A byte with value 0 means that there aren't any actors at this position
     * A byte with value 1 means that there is an obstacle at this position
     * A byte with value 2 means that there is a treasure at this position
     * A byte with value 3 means that there is a minion at this position
     * A byte with value 4 means that there is a BOSS enemy at this position
     * <h2>2 PLAYERS</h2>
     * Bytes 12-98 are reserved for 2 players and player+treasure:
     * x/10 is the ID of the first player, and x%10 is the ID of the second player(where x is the byte)
     * Bytes 10,20,...,90 are reserved to represent that there is only 1 player on this location(with ID x/10)
     * <h2>PLAYER+TREASURE</h2>
     * Since the treasure's type is randomly generated upon its pickup, the player+treasure positions are marked
     * with the bytes xx, where x/10(or x%10) is the ID of the player who is on the position
     * <h2>PLAYER+MINION</h2>
     * There are 2 types of minions - ordinary minions and bosses.
     * Bytes 10x mean that there is a weak minion + a player on this position, where X is the player's ID
     * Bytes 11x mean that there is a BOSS + a player on this position, where X is the player's ID
     */

    private static final int MAP_FIELD_SIZE = 25;

    private static final int XP_TEXT_LOCATION_X = 220;
    private static final int XP_TEXT_LOCATION_Y = 550;
    private static final int XP_BAR_UPPER_CORNER_X = 20;
    private static final int XP_BAR_UPPER_CORNER_Y = 525;
    private static final int XP_BAR_WIDTH = 450;
    private static final int XP_BAR_HEIGHT = 30;
    private static final int LEVEL_LABEL_LOCATION_X = 195;
    private static final int LEVEL_LABEL_LOCATION_Y = 580;


    private static final byte EMPTY_SPACE_BYTE = 0;
    private static final byte OBSTACLE_BYTE = 1;
    private static final byte TREASURE_BYTE = 2;
    private static final byte MINION_BYTE = 3;
    private static final byte BOSS_BYTE = 4;
    private static final String EMPTY_SPACE_DRAWING = ".";
    private static final String OBSTACLE_DRAWING = "#";
    private static final String TREASURE_DRAWING = "T";
    private static final String MINION_DRAWING = "M";
    private static final String BOSS_DRAWING = "B";

    private static final Map<Byte, String> BASIC_SHAPES = Map.of(
            EMPTY_SPACE_BYTE, EMPTY_SPACE_DRAWING,
            OBSTACLE_BYTE, OBSTACLE_DRAWING,
            TREASURE_BYTE, TREASURE_DRAWING,
            MINION_BYTE, MINION_DRAWING,
            BOSS_BYTE, BOSS_DRAWING);

    private static final Font ONE_ACTOR_PER_POSITION_FONT = new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE);
    private static final Font TWO_ACTORS_PER_POSITION_FONT = new Font("Comic Sans", Font.BOLD, MAP_FIELD_SIZE / 2);

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
        renderMap(g2d);
        renderXPBar(g2d);
    }

    private void renderMap(Graphics2D g2d) {
        var mapFields = currentSegment.GameMap().getFields();
        g2d.setStroke(new BasicStroke(5));

        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE));
        for (int i = 0; i < mapFields.length; i++) {
            for (int j = 0; j < mapFields[i].length; j++) {
                renderByte(g2d, mapFields[i][j], MAP_FIELD_SIZE * i, MAP_FIELD_SIZE * (j + 1));
            }
        }
    }

    private void renderByte(Graphics2D g2d, byte b, int xPos, int yPos) {
        // One Actor
        g2d.setFont(ONE_ACTOR_PER_POSITION_FONT);
        // One NPC
        if (BASIC_SHAPES.containsKey(b)) {
            g2d.drawString(BASIC_SHAPES.get(b), xPos, yPos);
            return;
        }
        // One Player
        if (b % 10 == 0) {
            g2d.drawString(String.valueOf(b / 10), xPos, yPos);
            return;
        }

        // Two Actors
        g2d.setFont(TWO_ACTORS_PER_POSITION_FONT);
        // Player+treasure
        if (b <= 99 && b % 11 == 0) {
            int playerId = b / 10;
            g2d.drawString(String.valueOf(playerId), xPos, yPos);
            g2d.drawString(TREASURE_DRAWING, xPos + MAP_FIELD_SIZE / 2, yPos);
            return;
        }

        // Player+player
        if (b < 100) {
            int player1Id = b / 10;
            int player2Id = b % 10;
            g2d.drawString(String.valueOf(player1Id), xPos, yPos);
            g2d.drawString(String.valueOf(player2Id), xPos + MAP_FIELD_SIZE / 2, yPos);
            return;
        }

        // Player+minion
        if (b < 110) {
            int playerId = b - 100;
            g2d.drawString(String.valueOf(playerId), xPos, yPos);
            g2d.drawString(MINION_DRAWING, xPos + MAP_FIELD_SIZE / 2, yPos);
            return;
        }

        // Player+boss
        int playerId = b - 110;
        g2d.drawString(String.valueOf(playerId), xPos, yPos);
        g2d.drawString(BOSS_DRAWING, xPos + MAP_FIELD_SIZE / 2, yPos);
    }

    private void renderXPBar(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLUE);
        g2d.drawRect(XP_BAR_UPPER_CORNER_X, XP_BAR_UPPER_CORNER_Y, XP_BAR_WIDTH, XP_BAR_HEIGHT);

        String levelLabel = "Level ".concat(String.valueOf(currentSegment.playerData().currentLevel()));
        String currentXPPercentage = String.valueOf(currentSegment.playerData().experiencePercentage());

        g2d.drawString(currentXPPercentage, XP_TEXT_LOCATION_X, XP_TEXT_LOCATION_Y);
        g2d.drawString(levelLabel, LEVEL_LABEL_LOCATION_X, LEVEL_LABEL_LOCATION_Y);

    }
}
