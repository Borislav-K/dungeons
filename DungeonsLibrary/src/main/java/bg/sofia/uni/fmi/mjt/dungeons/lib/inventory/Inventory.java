package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.*;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inventory implements Transmissible {

    private static final int INVENTORY_SIZE = 9;
    private static final Random random = new Random();
    private List<Item> items;

    public Inventory() {
        items = new ArrayList<>(INVENTORY_SIZE);
    }

    public void addItemToInventory(Item item) {
        if (items.size() < INVENTORY_SIZE) {
            items.add(item);
        }
    }

    public int currentSize() {
        return items.size();
    }

    // Items are 1-9, indexes of the list are 0-8
    public Item getItem(int itemNumber) {
        return itemNumber <= items.size() ? items.get(itemNumber - 1) : null;
    }

    public void removeRandomItem() {
        if (!items.isEmpty()) {
            items.remove(random.nextInt(items.size()));
        }
    }

    public Item removeItem(int itemNumber) {
        return itemNumber <= items.size() ? items.remove(itemNumber - 1) : null;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(items.size());
        for (Item item : items) {
            out.writeInt(item.type().ordinal());
            item.serialize(out);
        }
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        int itemsCount = in.readInt();
        for (int i = 1; i <= itemsCount; i++) {
            items.add(deserializeSingleItem(in));
        }
    }

    private Item deserializeSingleItem(DataInputStream in) throws IOException {
        ItemType itemType = ItemType.values()[in.readInt()];
        Item item = null;
        switch (itemType) {
            case HEALTH_POTION -> item = new HealthPotion();
            case MANA_POTION -> item = new ManaPotion();
            case WEAPON -> {
                Weapon weapon = new Weapon();
                weapon.deserialize(in);
                item = weapon;
            }
            case SPELL -> {
                Spell spell = new Spell();
                spell.deserialize(in);
                item = spell;
            }
        }
        return item;
    }
}
