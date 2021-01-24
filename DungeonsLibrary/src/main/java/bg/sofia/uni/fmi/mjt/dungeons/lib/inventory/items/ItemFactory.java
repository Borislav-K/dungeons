package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.util.List;
import java.util.Random;

public class ItemFactory {

    // Weapons
    private static final Weapon HAMMER = new Weapon(3, 20);
    private static final Weapon SWORD = new Weapon(5, 40);
    private static final List<Weapon> ALL_WEAPONS = List.of(HAMMER, SWORD);

    // Spells
    private static final Spell FIREBALL = new Spell(2, 40, 80);
    private static final Spell POISONOUS_BOLT = new Spell(5, 60, 100);
    private static final Spell COSMIC_BLAST = new Spell(8, 100, 150);
    private static final List<Spell> ALL_SPELLS = List.of(FIREBALL, POISONOUS_BOLT, COSMIC_BLAST);

    private static Random random = new Random();

    public static Item random() {
        ItemType itemType = ItemType.values()[random.nextInt(ItemType.values().length)]; // TODO Adjust randomization
        return ofType(itemType);
    }

    public static Item ofType(ItemType itemType) {
        return switch (itemType) {
            case HEALTH_POTION -> new HealthPotion();
            case MANA_POTION -> new ManaPotion();
            case WEAPON -> randomWeapon();
            case SPELL -> randomSpell();
        };
    }

    private static Weapon randomWeapon() {
        return ALL_WEAPONS.get(random.nextInt(ALL_WEAPONS.size()));
    }

    private static Spell randomSpell() {
        return ALL_SPELLS.get(random.nextInt(ALL_SPELLS.size()));
    }
}
