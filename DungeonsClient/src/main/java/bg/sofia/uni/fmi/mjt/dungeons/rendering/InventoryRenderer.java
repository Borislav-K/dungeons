package bg.sofia.uni.fmi.mjt.dungeons.rendering;

import bg.sofia.uni.fmi.mjt.dungeons.lib.exceptions.ItemNumberOutOfBoundsException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.Inventory;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Weapon;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InventoryRenderer {

    private static final ImageRepository imageRepository = ImageRepository.getInstance();

    public static void renderInventory(Graphics2D g2d, Inventory inventory) {
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

        // Items
        for (int i = 1; i <= inventory.currentSize(); i++) {
            int x = 550 + ((i - 1) % 3) * 30;
            int y = 300 + ((i - 1) / 3) * 30;
            try {
                Item currentItem = inventory.getItem(i);
                BufferedImage imageToDraw = switch (currentItem.type()) {
                    case HEALTH_POTION -> imageRepository.healthPotionImage();
                    case MANA_POTION -> imageRepository.manaPotionImage();
                    case WEAPON -> imageRepository.weaponPictureForLevel(((Weapon) currentItem).level());
                    case SPELL -> imageRepository.spellPictureForLevel(((Spell) currentItem).level());
                };
                g2d.drawImage(imageToDraw, x, y, null);
            } catch (ItemNumberOutOfBoundsException e) {
                throw new RuntimeException("Inconsistent relation between inventory size and actual items count");
            }
        }
    }
}
