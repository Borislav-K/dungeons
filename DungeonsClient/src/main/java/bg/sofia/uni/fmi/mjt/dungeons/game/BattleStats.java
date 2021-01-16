package bg.sofia.uni.fmi.mjt.dungeons.game;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BattleStats implements Externalizable {

    private static final long serialVersionUID = 1;

    private int health;
    private int currentHealth;
    private int mana;
    private int currentMana;
    private int attack;
    private int defense;

    public BattleStats() {
    }

    @Override
    public void writeExternal(ObjectOutput out) {
        throw new UnsupportedOperationException("BattleStats should only be read from the server");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.health = in.readInt();
        this.currentHealth = in.readInt();
        this.mana = in.readInt();
        this.currentMana = in.readInt();
        this.attack = in.readInt();
        this.defense = in.readInt();
    }

    public int health() {
        return health;
    }

    public int currentHealth() {
        return currentHealth;
    }

    public int mana() {
        return mana;
    }

    public int currentMana() {
        return currentMana;
    }

    public int attack() {
        return attack;
    }

    public int defense() {
        return defense;
    }
}
