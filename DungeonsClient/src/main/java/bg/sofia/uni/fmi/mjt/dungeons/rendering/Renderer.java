package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.game.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.network.PlayerSegment;

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
     * Bytes 10x mean that there is a minion + a player on this position, where X is the player's ID
     */

    private static final int MAP_FIELD_SIZE = 25;
    private static final int BATTLESTATS_LABELS_FONT_SIZE = 20;

    // Experience and bar
    private static final int XP_LABEL_LOCATION_X = 220;
    private static final int XP_LABEL_LOCATION_Y = 550;
    private static final int XP_BAR_UPPER_CORNER_X = 20;
    private static final int XP_BAR_UPPER_CORNER_Y = 525;
    private static final int XP_BAR_WIDTH = 450;
    private static final int XP_BAR_HEIGHT = 30;
    private static final int XP_BAR_ARC = 10;
    private static final int LEVEL_LABEL_LOCATION_X = 195;
    private static final int LEVEL_LABEL_LOCATION_Y = 580;

    // Battlestats bars
    private static final int BATTLESTATS_BARS_WIDTH = 125;
    private static final int BATTLESTATS_BARS_HEIGHT = 18;
    private static final int BATTLESTATS_LABEL_LOCATION_X = 555;
    private static final int BATTLESTATS_TEXT_LOCATION_X = 555;
    private static final int BATTLESTATS_BAR_LOCATION_X = 525;

    // Health bar
    private static final String HEALTH_LABEL = "Health";
    private static final int HEALTH_LABEL_LOCATION_Y = 50;
    private static final int HEALTH_TEXT_LOCATION_Y = 70;
    private static final int HEALTH_BAR_LOCATION_Y = 55;

    // Mana bar
    private static final String MANA_LABEL = "Mana";
    private static final int MANA_LABEL_LOCATION_Y = 100;
    private static final int MANA_TEXT_LOCATION_Y = 120;
    private static final int MANA_BAR_LOCATION_Y = 105;

    // Attack Label & text
    private static final String ATTACK_LABEL = "Attack Points";
    private static final int ATTACK_LABEL_LOCATION_X = 520;
    private static final int ATTACK_LABEL_LOCATION_Y = 150;
    private static final int ATTACK_TEXT_LOCATION_Y = 180;
    // Defense Label & text
    private static final String DEFENSE_LABEL = "Defense points";
    private static final int DEFENSE_LABEL_LOCATION_X = 520;
    private static final int DEFENSE_LABEL_LOCATION_Y = 200;
    private static final int DEFENSE_TEXT_LOCATION_Y = 230;

    private static final byte EMPTY_SPACE_BYTE = 0;
    private static final byte OBSTACLE_BYTE = 1;
    private static final byte TREASURE_BYTE = 2;
    private static final byte MINION_BYTE = 3;
    private static final String EMPTY_SPACE_DRAWING = ".";
    private static final String OBSTACLE_DRAWING = "#";
    private static final String TREASURE_DRAWING = "T";
    private static final String MINION_DRAWING = "M";

    private static final Map<Byte, String> BASIC_SHAPES = Map.of(
            EMPTY_SPACE_BYTE, EMPTY_SPACE_DRAWING,
            OBSTACLE_BYTE, OBSTACLE_DRAWING,
            TREASURE_BYTE, TREASURE_DRAWING,
            MINION_BYTE, MINION_DRAWING);

    private static final Font ONE_ACTOR_PER_POSITION_FONT = new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE);
    private static final Font TWO_ACTORS_PER_POSITION_FONT = new Font("Comic Sans", Font.BOLD, MAP_FIELD_SIZE / 2);
    private static final Font BATTLESTATS_FONT = new Font("Comic Sans", Font.BOLD, BATTLESTATS_LABELS_FONT_SIZE);

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
        renderBattleStats(g2d);
    }

    private void renderMap(Graphics2D g2d) {
        var mapFields = currentSegment.GameMap().getFields();
        g2d.setStroke(new BasicStroke(5));

        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE));
        for (int i = 0; i < mapFields.length; i++) {
            for (int j = 0; j < mapFields[i].length; j++) {
                renderMapByte(g2d, mapFields[i][j], MAP_FIELD_SIZE * i, MAP_FIELD_SIZE * (j + 1));
            }
        }
    }

    private void renderMapByte(Graphics2D g2d, byte b, int xPos, int yPos) {
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
        int playerId = b - 100;
        g2d.drawString(String.valueOf(playerId), xPos, yPos);
        g2d.drawString(MINION_DRAWING, xPos + MAP_FIELD_SIZE / 2, yPos);

    }

    private void renderXPBar(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));

        String levelLabel = "Level ".concat(String.valueOf(currentSegment.playerData().currentLevel()));
        int currentXPPercentage = currentSegment.playerData().experiencePercentage();

        //XP Bar progress
        g2d.setColor(Color.YELLOW);
        int progressWidth = (int) Math.round(XP_BAR_WIDTH * (currentXPPercentage / 100.0));
        g2d.fillRoundRect(XP_BAR_UPPER_CORNER_X, XP_BAR_UPPER_CORNER_Y, progressWidth, XP_BAR_HEIGHT, XP_BAR_ARC, XP_BAR_ARC);
        //XP Bar Border
        g2d.setColor(Color.BLUE);
        g2d.drawRoundRect(XP_BAR_UPPER_CORNER_X, XP_BAR_UPPER_CORNER_Y, XP_BAR_WIDTH, XP_BAR_HEIGHT, XP_BAR_ARC, XP_BAR_ARC);
        // Level Label
        g2d.drawString(String.valueOf(currentXPPercentage).concat("%"), XP_LABEL_LOCATION_X, XP_LABEL_LOCATION_Y);
        g2d.drawString(levelLabel, LEVEL_LABEL_LOCATION_X, LEVEL_LABEL_LOCATION_Y);


    }

    private void renderBattleStats(Graphics2D g2d) {
        BattleStats battleStats = currentSegment.playerData().battleStats();

        g2d.setFont(BATTLESTATS_FONT);

        // Health Bar
        g2d.setColor(Color.GREEN);
        g2d.drawRect(BATTLESTATS_BAR_LOCATION_X, HEALTH_BAR_LOCATION_Y, BATTLESTATS_BARS_WIDTH, BATTLESTATS_BARS_HEIGHT);
        int barWidth = BATTLESTATS_BARS_WIDTH * battleStats.currentHealth() / battleStats.health();
        g2d.fillRect(BATTLESTATS_BAR_LOCATION_X, HEALTH_BAR_LOCATION_Y, barWidth, BATTLESTATS_BARS_HEIGHT);

        g2d.setColor(Color.black);
        g2d.drawString(HEALTH_LABEL, BATTLESTATS_LABEL_LOCATION_X, HEALTH_LABEL_LOCATION_Y);
        String healthText = String.valueOf(battleStats.currentHealth())
                .concat("/")
                .concat(String.valueOf(battleStats.health()));
        g2d.drawString(healthText, BATTLESTATS_TEXT_LOCATION_X, HEALTH_TEXT_LOCATION_Y);

        // Mana bar
        g2d.setColor(Color.BLUE);
        g2d.drawRect(BATTLESTATS_BAR_LOCATION_X, MANA_BAR_LOCATION_Y, BATTLESTATS_BARS_WIDTH, BATTLESTATS_BARS_HEIGHT);
        barWidth = BATTLESTATS_BARS_WIDTH * battleStats.currentMana() / battleStats.mana();
        g2d.fillRect(BATTLESTATS_BAR_LOCATION_X, MANA_BAR_LOCATION_Y, barWidth, BATTLESTATS_BARS_HEIGHT);

        g2d.setColor(Color.black);
        g2d.drawString(MANA_LABEL, BATTLESTATS_LABEL_LOCATION_X, MANA_LABEL_LOCATION_Y);
        String manaText = String.valueOf(battleStats.currentMana())
                .concat("/")
                .concat(String.valueOf(battleStats.mana()));
        g2d.drawString(manaText, BATTLESTATS_TEXT_LOCATION_X, MANA_TEXT_LOCATION_Y);

        // Attack and defense labels
        g2d.setColor(Color.black);
        g2d.drawString(ATTACK_LABEL, ATTACK_LABEL_LOCATION_X, ATTACK_LABEL_LOCATION_Y);
        g2d.drawString(DEFENSE_LABEL, DEFENSE_LABEL_LOCATION_X, DEFENSE_LABEL_LOCATION_Y);

        // Attack and defense points
        g2d.drawString(String.valueOf(battleStats.attack()), BATTLESTATS_TEXT_LOCATION_X, ATTACK_TEXT_LOCATION_Y);
        g2d.drawString(String.valueOf(battleStats.defense()), BATTLESTATS_TEXT_LOCATION_X, DEFENSE_TEXT_LOCATION_Y);
    }
}
