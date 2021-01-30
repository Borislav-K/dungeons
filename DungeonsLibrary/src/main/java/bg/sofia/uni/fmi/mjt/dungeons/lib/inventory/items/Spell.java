package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

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
        out.writeByte(level);
        out.writeShort(damage);
        out.writeShort(manaCost);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        level = in.readByte();
        damage = in.readShort();
        manaCost = in.readShort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spell spell = (Spell) o;
        return level == spell.level && damage == spell.damage && manaCost == spell.manaCost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, damage, manaCost);
    }
}
