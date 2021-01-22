package bg.sofia.uni.fmi.mjt.dungeons.lib;

import bg.sofia.uni.fmi.mjt.dungeons.lib.network.Transmissible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BattleStats implements Transmissible {

    private int health;
    private int currentHealth;
    private int mana;
    private int currentMana;
    private int attack;
    private int defense;

    public BattleStats() {
    }

    public BattleStats(int health, int mana, int attack, int defense) {
        this.health = health;
        this.currentHealth = health;
        this.mana = mana;
        this.currentMana = mana;
        this.attack = attack;
        this.defense = defense;
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

    public void takeDamage(int amount) {
        int diminishedAmount = amount - defense;
        if (diminishedAmount > 0) {
            currentHealth = Math.max(0, currentHealth - diminishedAmount);
        }
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(health);
        out.writeInt(currentHealth);
        out.writeInt(mana);
        out.writeInt(currentMana);
        out.writeInt(attack);
        out.writeInt(defense);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.health = in.readInt();
        this.currentHealth = in.readInt();
        this.mana = in.readInt();
        this.currentMana = in.readInt();
        this.attack = in.readInt();
        this.defense = in.readInt();
    }

    @Override
    public String toString() {
        return "BattleStats{" +
               "health=" + health +
               ", currentHealth=" + currentHealth +
               ", mana=" + mana +
               ", currentMana=" + currentMana +
               ", attack=" + attack +
               ", defense=" + defense +
               '}';
    }
}
