package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Item extends Transmissible {

    ItemType type();

    @Override
    default void serialize(DataOutputStream out) throws IOException {

    }

    @Override
    default void deserialize(DataInputStream in) throws IOException {

    }
}
