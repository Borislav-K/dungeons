package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

public class ManaPotion extends Item{
    @Override
    public ItemType type() {
        return ItemType.MANA_POTION;
    }
}
