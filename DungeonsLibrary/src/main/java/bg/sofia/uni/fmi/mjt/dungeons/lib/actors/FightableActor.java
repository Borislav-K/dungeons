package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public abstract class FightableActor implements Actor {

    protected Position2D position;
    protected int health;
    protected int currentHealth;
    protected int mana;
    protected int currentMana;
    protected int attack;
    protected int defense;

    protected FightableActor() {

    }

    protected FightableActor(int health, int mana, int attack, int defense) {
        setStats(health, mana, attack, defense);
    }

    protected void setStats(int health, int mana, int attack, int defense) {
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
        assertNotNegative(amount);
        int diminishedAmount = amount - defense;
        if (diminishedAmount > 0) {
            currentHealth = Math.max(0, currentHealth - diminishedAmount);
        }
    }

    public void heal(int amount) {
        assertNotNegative(amount);
        currentHealth = Math.min(health, currentHealth + amount);
    }

    public void drainMana(int amount) {
        assertNotNegative(amount);
        currentMana = Math.max(0, currentMana - amount);
    }

    public void replenish(int amount) {
        assertNotNegative(amount);
        currentMana = Math.min(mana, currentMana + amount);
    }

    public abstract int XPReward();

    public abstract void dealDamage(FightableActor subject);

    @Override
    public Position2D position() {
        return position;
    }

    @Override
    public void setPosition(Position2D position) {
        this.position = position;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeShort(health);
        out.writeShort(currentHealth);
        out.writeShort(mana);
        out.writeShort(currentMana);
        out.writeShort(attack);
        out.writeShort(defense);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        this.health = in.readShort();
        this.currentHealth = in.readShort();
        this.mana = in.readShort();
        this.currentMana = in.readShort();
        this.attack = in.readShort();
        this.defense = in.readShort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FightableActor that = (FightableActor) o;
        return health == that.health && currentHealth == that.currentHealth && mana == that.mana
                && currentMana == that.currentMana && attack == that.attack && defense == that.defense
                && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, health, currentHealth, mana, currentMana, attack, defense);
    }

    protected static void assertNotNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be >=0");
        }
    }
}
