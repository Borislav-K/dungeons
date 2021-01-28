package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.io.IOException;
import java.nio.ByteBuffer;

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
    public void serialize(ByteBuffer out) throws IOException {
        out.putInt(level);
        out.putInt(damage);
        out.putInt(manaCost);
    }

    @Override
    public void deserialize(ByteBuffer in) throws IOException {
        level = in.getInt();
        damage = in.getInt();
        manaCost = in.getInt();
    }
}
