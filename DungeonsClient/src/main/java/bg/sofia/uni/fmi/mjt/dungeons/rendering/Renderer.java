package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.exceptions.ItemNumberOutOfBoundsException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.Inventory;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.DefaultPlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.MAP_DIMENSIONS;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.OBSTACLE_POSITIONS;

public class Renderer extends JPanel {

    private static final int MAP_FIELD_SIZE = 25;
    private static final Font BATTLESTATS_FONT = new Font("Comic Sans", Font.BOLD, 20);

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


    private PlayerSegment lastReceivedSegment;

    private BufferedImage obstacleImage;
    private BufferedImage treasureImage;
    private BufferedImage healthPotionImage;
    private BufferedImage manaPotionImage;
    private List<BufferedImage> minionPictures;
    private List<BufferedImage> playerPictures;

    public Renderer() {
        super.setBackground(Color.white);
        this.lastReceivedSegment = null;
        initializeResources();
    }

    private void initializeResources() {
        minionPictures = new ArrayList<>();
        playerPictures = new ArrayList<>();
        try {
            obstacleImage = ImageIO.read(new File("DungeonsClient/src/main/resources/obstacle.bmp"));
            treasureImage = ImageIO.read(new File("DungeonsClient/src/main/resources/treasure.bmp"));
            healthPotionImage = ImageIO.read(new File("DungeonsClient/src/main/resources/health_potion.bmp"));
            manaPotionImage = ImageIO.read(new File("DungeonsClient/src/main/resources/mana_potion.bmp"));
            for (int i = 1; i <= 5; i++) {
                File imageFile = new File("DungeonsClient/src/main/resources/minion_level%d.bmp".formatted(i));
                minionPictures.add(ImageIO.read(imageFile));
            }
            for (int i = 1; i <= 9; i++) {
                File imageFile = new File("DungeonsClient/src/main/resources/player%d.bmp".formatted(i));
                playerPictures.add(ImageIO.read(imageFile));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load a resource", e);
        }
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
            Player currentPlayer = ((DefaultPlayerSegment) lastReceivedSegment).player();
            renderMap(g2d);
            renderXPBar(g2d, currentPlayer.level(), currentPlayer.XPPercentage());
            renderBattleStats(g2d, currentPlayer.stats());
            renderInventory(g2d, currentPlayer.inventory());
        }
    }

    private void renderNotConnectedMessage(Graphics2D g2d) {
        g2d.drawString("Not connected to the server yet...", 250, 250);
    }

    private void renderDeathMessage(Graphics2D g2d) {
        g2d.drawString("YOU DIED", 250, 250);
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
                    g2d.drawImage(obstacleImage, i * MAP_FIELD_SIZE, j * MAP_FIELD_SIZE, null);
                }
            }
        }
    }

    private void drawObstacles(Graphics2D g2d) {
        for (int obstacle : OBSTACLE_POSITIONS) {
            int x = (obstacle / MAP_DIMENSIONS) * MAP_FIELD_SIZE;
            int y = (obstacle % MAP_DIMENSIONS) * MAP_FIELD_SIZE;
            g2d.drawImage(obstacleImage, x, y, null);
        }
    }

    private void drawActors(Graphics2D g2d) {
        DefaultPlayerSegment playerSegmentCast = (DefaultPlayerSegment) lastReceivedSegment;
        playerSegmentCast.positionsWithActors().forEach(position -> drawPosition(g2d, position));
    }

    private void drawPosition(Graphics2D g2d, Position2D pos) {
        List<Actor> actors = pos.actors();
        boolean isAloneActorOnPosition = actors.size() < 2;
        int x = pos.x() * MAP_FIELD_SIZE;
        int y = pos.y() * MAP_FIELD_SIZE;

        drawActor(g2d, actors.get(0), isAloneActorOnPosition, x, y);
        if (!isAloneActorOnPosition) {
            drawActor(g2d, actors.get(1), false, x + MAP_FIELD_SIZE / 2, y);
        }
    }

    private void drawActor(Graphics2D g2d, Actor actor, boolean isAloneOnPosition, int x, int y) {
        BufferedImage imageToDraw = switch (actor.type()) {
            case TREASURE -> treasureImage;
            case MINION -> minionPictures.get(((Minion) actor).level() - 1);
            case PLAYER -> playerPictures.get(((Player) actor).id() - 1);
        };
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        if (!isAloneOnPosition) {
            at.scale(0.5, 1);
        }
        g2d.drawImage(imageToDraw, at, null);
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

    private void renderInventory(Graphics2D g2d, Inventory inventory) {
        // Inventory grid
        g2d.setColor(Color.WHITE);
        g2d.fillRect(550, 300, 90, 90);
        g2d.setColor(Color.black);
        g2d.drawRect(550, 300, 90, 90);
        g2d.drawRect(550, 300, 30, 30);
        g2d.drawRect(580, 330, 30, 30);
        g2d.drawRect(610, 360, 30, 30);
        g2d.drawRect(550, 360, 30, 30);
        g2d.drawRect(610, 300, 30, 30);

        for (int i = 1; i <= inventory.currentSize(); i++) {
            int x = 550 + ((i-1) % 3) * 30;
            int y = 300 + ((i-1) / 3) * 30;
            try {
                Item currentItem = inventory.getItem(i);
                BufferedImage imageToDraw = switch (currentItem.type()) {
                    case HEALTH_POTION -> healthPotionImage;
                    case MANA_POTION -> manaPotionImage;
                };
                g2d.drawImage(imageToDraw, x, y, null);
            } catch (ItemNumberOutOfBoundsException e) {
                throw new RuntimeException("Inconsistent relation between inventory size and actual items count");
            }
        }
    }

}

