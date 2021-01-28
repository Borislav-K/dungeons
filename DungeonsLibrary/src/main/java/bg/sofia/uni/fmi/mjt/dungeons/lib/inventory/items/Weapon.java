package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;

import java.io.IOException;
import java.nio.ByteBuffer;

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
    public void serialize(ByteBuffer out) throws IOException {
        out.putInt(level);
        out.putInt(attack);
    }

    @Override
    public void deserialize(ByteBuffer in) throws IOException {
        level = in.getInt();
        attack = in.getInt();
    }
}
