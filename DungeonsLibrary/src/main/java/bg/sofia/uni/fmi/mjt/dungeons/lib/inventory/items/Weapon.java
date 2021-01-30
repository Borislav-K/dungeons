package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Weapon implements Item {

    private int level;
    private int attack;

    public Weapon() {
    }

    public Weapon(int level, int attack) {
        this.level = level;
        this.attack = attack;
    }

    @Override
    public ItemType type() {
        return ItemType.WEAPON;
    }

    public int level() {
        return level;
    }

    public int attack() {
        return attack;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeByte(level);
        out.writeShort(attack);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        level = in.readByte();
        attack = in.readShort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weapon weapon = (Weapon) o;
        return level == weapon.level && attack == weapon.attack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, attack);
    }
}
