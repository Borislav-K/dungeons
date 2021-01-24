package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Spell implements Item {

    private int level;
    private int damage;
    private int manaCost;

    public Spell() {
    }

    public Spell(int level, int damage, int manaCost) {
        this.level = level;
        this.damage = damage;
        this.manaCost = manaCost;
    }

    @Override
    public ItemType type() {
        return ItemType.SPELL;
    }

    public int level() {
        return level;
    }

    public int damage() {
        return damage;
    }

    public int manaCost() {
        return manaCost;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(level);
        out.writeInt(damage);
        out.writeInt(manaCost);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        level = in.readInt();
        damage = in.readInt();
        manaCost = in.readInt();
    }
}
