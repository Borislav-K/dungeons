package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

public class HealthPotion extends Item {
    @Override
    public ItemType type() {
        return ItemType.HEALTH_POTION;
    }
}
