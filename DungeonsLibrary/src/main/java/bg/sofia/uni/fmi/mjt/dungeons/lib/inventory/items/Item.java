package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;

public interface Item extends Transmissible {

    ItemType type();
}
