package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.MAP_DIMENSIONS;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.OBSTACLE_POSITIONS;

public class Renderer extends JPanel {

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

    private static final String OBSTACLE_DRAWING = "#";
    private static final String TREASURE_DRAWING = "T";
    private static final String MINION_DRAWING = "M";

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

        Player currentPlayer = currentSegment.player();
        renderXPBar(g2d, currentPlayer.level(), currentPlayer.XPPercentage());
        renderBattleStats(g2d, currentPlayer.battleStats());
    }

    private void renderMap(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.red);
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, MAP_FIELD_SIZE));

        drawBarrier(g2d);
        drawObstacles(g2d);
        drawActors(g2d);
    }

    private void drawBarrier(Graphics2D g2d) {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                if (i == 0 || j == 0 || i == MAP_DIMENSIONS - 1 || j == MAP_DIMENSIONS - 1) {
                    g2d.drawString(OBSTACLE_DRAWING, i * MAP_FIELD_SIZE, (j + 1) * MAP_FIELD_SIZE);
                }
            }
        }
    }

    private void drawObstacles(Graphics2D g2d) {
        for (int obstacle : OBSTACLE_POSITIONS) {
            int x = obstacle / MAP_DIMENSIONS;
            int y = obstacle % MAP_DIMENSIONS;
            g2d.drawString(OBSTACLE_DRAWING, x * MAP_FIELD_SIZE, (y + 1) * MAP_FIELD_SIZE);
        }
    }

    private void drawActors(Graphics2D g2d) {
        currentSegment.positionsWithActors().forEach(position -> drawPosition(g2d, position));
    }

    private void drawPosition(Graphics g2d, Position2D pos) {
        List<Actor> actors = pos.actors();
        g2d.setFont(actors.size() == 2 ? TWO_ACTORS_PER_POSITION_FONT : ONE_ACTOR_PER_POSITION_FONT);
        int xDrawCoords = pos.x() * MAP_FIELD_SIZE;
        int yDrawCoords = (pos.y() + 1) * MAP_FIELD_SIZE;

        String stringToDraw = "";
        for (Actor actor : actors) {
            String actorDrawing = switch (actor.type()) {
                case MINION -> MINION_DRAWING;
                case PLAYER -> String.valueOf(((Player) actor).id());
                case TREASURE -> TREASURE_DRAWING;
            };
            stringToDraw = stringToDraw.concat(actorDrawing);
        }
        g2d.drawString(stringToDraw, xDrawCoords, yDrawCoords);
    }

    private void renderXPBar(Graphics2D g2d, int level, int XPPercentage) {
        g2d.setStroke(new BasicStroke(2));

        String levelLabel = "Level ".concat(String.valueOf(level));

        //XP Bar progress
        g2d.setColor(Color.YELLOW);
        int progressWidth = (int) Math.round(XP_BAR_WIDTH * (XPPercentage / 100.0));
        g2d.fillRoundRect(XP_BAR_UPPER_CORNER_X, XP_BAR_UPPER_CORNER_Y, progressWidth, XP_BAR_HEIGHT, XP_BAR_ARC, XP_BAR_ARC);
        //XP Bar Border
        g2d.setColor(Color.BLUE);
        g2d.drawRoundRect(XP_BAR_UPPER_CORNER_X, XP_BAR_UPPER_CORNER_Y, XP_BAR_WIDTH, XP_BAR_HEIGHT, XP_BAR_ARC, XP_BAR_ARC);
        // Level Label
        g2d.drawString(String.valueOf(XPPercentage).concat("%"), XP_LABEL_LOCATION_X, XP_LABEL_LOCATION_Y);
        g2d.drawString(levelLabel, LEVEL_LABEL_LOCATION_X, LEVEL_LABEL_LOCATION_Y);

    }

    private void renderBattleStats(Graphics2D g2d, BattleStats battleStats) {
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

