package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.util.Random;

public class ItemFactory {

    private static Random random = new Random();

    public static Item random() {
        ItemType itemType = ItemType.values()[random.nextInt(ItemType.values().length)];
        System.out.println("Creating an item of type"+itemType.toString());
        return ofType(itemType);
    }

    public static Item ofType(ItemType itemType) {
        return switch (itemType) {
            case HEALTH_POTION -> new HealthPotion();
            case MANA_POTION -> new ManaPotion();
        };
    }
}
