package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Weapon;

import java.awt.*;

public class PlayerDataRenderer {

    private static final ImageRepository imageRepository = ImageRepository.getInstance();

    public static void renderPlayerStats(Graphics2D g2d, Player player) {
        renderXPBar(g2d, player.level(), player.XPPercentage());
        renderBattleStats(g2d, player);
    }

    private static void renderXPBar(Graphics2D g2d, int level, int XPPercentage) {
        g2d.setStroke(new BasicStroke(2));

        //XP Bar progress
        g2d.setColor(Color.YELLOW);
        int progressWidth = (int) Math.round(450 * (XPPercentage / 100.0));
        g2d.fillRoundRect(20, 525, progressWidth, 30, 10, 10);
        //XP Bar Border
        g2d.setColor(Color.BLUE);
        g2d.drawRoundRect(20, 525, 450, 30, 10, 10);
        // Level Label
        g2d.drawString("%d%%".formatted(XPPercentage), 220, 550);
        g2d.drawString("Level %d".formatted(level), 195, 580);

    }

    private static void renderBattleStats(Graphics2D g2d, Player player) {
        g2d.setFont(new Font("Comic Sans", Font.BOLD, 20));

        // Health Bar
        g2d.setColor(Color.GREEN);
        g2d.drawRect(525, 55, 125, 18);
        int barWidth = 125 * player.currentHealth() / player.health();
        g2d.fillRect(525, 55, barWidth, 18);

        // Mana bar
        g2d.setColor(Color.BLUE);
        g2d.drawRect(525, 105, 125, 18);
        barWidth = 125 * player.currentMana() / player.mana();
        g2d.fillRect(525, 105, barWidth, 18);

        // Health Label
        g2d.setColor(Color.black);
        g2d.drawString("Health", 555, 50);
        g2d.drawString("%d/%d".formatted(player.currentHealth(), player.health()), 555, 70);

        // Mana label
        g2d.drawString("Mana", 555, 100);
        g2d.drawString("%d/%d".formatted(player.currentMana(), player.mana()), 555, 120);

        // Attack and defense icons
        g2d.drawImage(imageRepository.attackPointsIcon(), 520, 150, null);
        g2d.drawImage(imageRepository.defensePointsIcon(), 520, 200, null);

        // Attack and defense points
        g2d.drawString(String.valueOf(player.attack()), 550, 170);
        g2d.drawString(String.valueOf(player.defense()), 550, 220);

        // Weapon and spell bonus damage
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

        // Weapon image
        if (weapon != null) {
            g2d.drawImage(imageRepository.weaponPictureForLevel(weapon.level()), 605, 230, null);
        }
        // Spell image
        if (spell != null) {
            g2d.drawImage(imageRepository.spellPictureForLevel(spell.level()), 580, 255, null);
        }
    }
}
