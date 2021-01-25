package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Weapon;

import java.awt.*;

public class PlayerDataRenderer {

    private static final ImageRepository imageRepository = ImageRepository.getInstance();

    // Experience bar
    private static final int XP_LABEL_LOCATION_X = 220;
    private static final int XP_LABEL_LOCATION_Y = 550;
    private static final int XP_BAR_UPPER_CORNER_X = 20;
    private static final int XP_BAR_UPPER_CORNER_Y = 525;
    private static final int XP_BAR_WIDTH = 450;
    private static final int XP_BAR_HEIGHT = 30;
    private static final int XP_BAR_ARC = 10;
    private static final int LEVEL_LABEL_LOCATION_X = 195;
    private static final int LEVEL_LABEL_LOCATION_Y = 580;

    // Battlestats
    private static final int STATS_BARS_WIDTH = 125;
    private static final int STATS_BARS_HEIGHT = 18;
    private static final int STATS_LABEL_LOCATION_X = 555;
    private static final int STATS_TEXT_LOCATION_X = 555;
    private static final int STATS_BAR_LOCATION_Y = 525;

    // Health bar
    private static final int HEALTH_LABEL_LOCATION_Y = 50;
    private static final int HEALTH_TEXT_LOCATION_Y = 70;
    private static final int HEALTH_BAR_LOCATION_Y = 55;

    // Mana bar
    private static final int MANA_LABEL_LOCATION_Y = 100;
    private static final int MANA_TEXT_LOCATION_Y = 120;
    private static final int MANA_BAR_LOCATION_Y = 105;

    public static void renderPlayerStats(Graphics2D g2d, Player player) {
        renderXPBar(g2d, player.level(), player.XPPercentage());
        renderBattleStats(g2d, player);
    }

    private static void renderXPBar(Graphics2D g2d, int level, int XPPercentage) {
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

    private static void renderBattleStats(Graphics2D g2d, Player player) {
        g2d.setFont(new Font("Comic Sans", Font.BOLD, 20));

        // Health Bar
        g2d.setColor(Color.GREEN);
        g2d.drawRect(STATS_BAR_LOCATION_Y, HEALTH_BAR_LOCATION_Y, STATS_BARS_WIDTH, STATS_BARS_HEIGHT);
        int barWidth = STATS_BARS_WIDTH * player.currentHealth() / player.health();
        g2d.fillRect(STATS_BAR_LOCATION_Y, HEALTH_BAR_LOCATION_Y, barWidth, STATS_BARS_HEIGHT);

        g2d.setColor(Color.black);
        g2d.drawString("Health", STATS_LABEL_LOCATION_X, HEALTH_LABEL_LOCATION_Y);
        String healthText = String.valueOf(player.currentHealth())
                .concat("/")
                .concat(String.valueOf(player.health()));
        g2d.drawString(healthText, STATS_TEXT_LOCATION_X, HEALTH_TEXT_LOCATION_Y);

        // Mana bar
        g2d.setColor(Color.BLUE);
        g2d.drawRect(STATS_BAR_LOCATION_Y, MANA_BAR_LOCATION_Y, STATS_BARS_WIDTH, STATS_BARS_HEIGHT);
        barWidth = STATS_BARS_WIDTH * player.currentMana() / player.mana();
        g2d.fillRect(STATS_BAR_LOCATION_Y, MANA_BAR_LOCATION_Y, barWidth, STATS_BARS_HEIGHT);

        g2d.setColor(Color.black);
        g2d.drawString("Mana", STATS_LABEL_LOCATION_X, MANA_LABEL_LOCATION_Y);
        String manaText = String.valueOf(player.currentMana())
                .concat("/")
                .concat(String.valueOf(player.mana()));
        g2d.drawString(manaText, STATS_TEXT_LOCATION_X, MANA_TEXT_LOCATION_Y);

        // Attack and defense icons
        g2d.setColor(Color.black);
        g2d.drawImage(imageRepository.attackPointsIcon(), 520, 150, null);
        g2d.drawImage(imageRepository.defensePointsIcon(), 520, 200, null);

        // Attack and defense points
        g2d.drawString(String.valueOf(player.attack()), 550, 170);
        g2d.drawString(String.valueOf(player.defense()), 550, 220);

        Weapon weapon = player.weapon();
        Spell spell = player.spell();

        int weaponDamage = weapon == null ? 0 : weapon.attack();
        int spellDamage = spell == null ? 0 : spell.damage();

        boolean willFightWithSpell = spell != null
                && spell.manaCost() <= player.currentMana()
                && spellDamage > weaponDamage;
        if (willFightWithSpell) {
            g2d.setColor(Color.BLUE);
            g2d.drawString("+%d".formatted(spellDamage), 590, 170);
        } else {
            g2d.setColor(Color.GREEN);
            g2d.drawString("+%d".formatted(weaponDamage), 590, 170);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawString("Weapon", 525, 250);
        g2d.drawString("Spell", 525, 275);
        if (weapon != null) {
            g2d.drawImage(imageRepository.weaponPictureForLevel(weapon.level()), 605, 230, null);
        }
        if (spell != null) {
            g2d.drawImage(imageRepository.spellPictureForLevel(spell.level()), 580, 255, null);
        }
    }
}
