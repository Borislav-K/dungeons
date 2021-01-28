package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.IOException;

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
    public void serialize(SmartBuffer out) throws IOException {
        out.writeInt(level);
        out.writeInt(attack);
    }

    @Override
    public void deserialize(SmartBuffer in) throws IOException {
        level = in.readInt();
        attack = in.readInt();
    }
}
