package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.util.Random;

public class HealthPotion implements Item {

    private static final Random random = new Random();

    private static final int MIN_HEALING = 20;
    private static final int MAX_HEALING = 70;

    @Override
    public ItemType type() {
        return ItemType.HEALTH_POTION;
    }

    public int healingAmount() {
        return random.nextInt(MAX_HEALING - MIN_HEALING) + MIN_HEALING;
    }
}
