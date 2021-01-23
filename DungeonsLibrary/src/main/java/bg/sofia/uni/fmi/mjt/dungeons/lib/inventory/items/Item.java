package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;

public abstract class Item implements Transmissible {

    public abstract ItemType type();
}
