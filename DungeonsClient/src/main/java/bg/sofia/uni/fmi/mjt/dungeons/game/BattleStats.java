package bg.sofia.uni.fmi.mjt.dungeons.game;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BattleStats {

    private int health;
    private int currentHealth;
    private int mana;
    private int currentMana;
    private int attack;
    private int defense;

    public BattleStats() {
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

    public void deserialize(DataInputStream in) throws IOException {
        this.health = in.readInt();
        this.currentHealth = in.readInt();
        this.mana = in.readInt();
        this.currentMana = in.readInt();
        this.attack = in.readInt();
        this.defense = in.readInt();
    }

}
