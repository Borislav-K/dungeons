package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.util.List;
import java.util.Random;

public class ItemFactory {

    //Weapons
    private static final Weapon HAMMER = new Weapon(3, 20);
    private static final Weapon SWORD = new Weapon(5, 40);

    private static final List<Weapon> ALL_WEAPONS = List.of(HAMMER, SWORD);

    private static Random random = new Random();

    public static Item random() {
        ItemType itemType = ItemType.values()[random.nextInt(ItemType.values().length)];

        System.out.println("Creating an item of type" + itemType.toString());
        return ofType(itemType);
    }

    public static Item ofType(ItemType itemType) {
        return switch (itemType) {
            case HEALTH_POTION -> new HealthPotion();
            case MANA_POTION -> new ManaPotion();
            case WEAPON -> randomWeapon();
        };
    }

    private static Weapon randomWeapon() {
        return ALL_WEAPONS.get(random.nextInt(ALL_WEAPONS.size()));
    }
}
