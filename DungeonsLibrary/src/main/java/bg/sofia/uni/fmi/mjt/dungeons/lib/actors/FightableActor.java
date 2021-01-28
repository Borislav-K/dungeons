package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;

import java.io.IOException;
import java.nio.ByteBuffer;

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
        int diminishedAmount = amount - defense;
        if (diminishedAmount > 0) {
            currentHealth = Math.max(0, currentHealth - diminishedAmount);
        }
    }

    public void heal(int amount) {
        currentHealth = Math.min(health, currentHealth + amount);
    }

    public void drainMana(int amount) {
        currentMana -= Math.max(0, amount);
    }

    public void replenish(int amount) {
        currentMana = Math.min(mana, currentMana + amount);
    }

    public abstract int XPReward();

    public abstract int level();

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
    public void serialize(ByteBuffer out) throws IOException {
        out.putInt(health);
        out.putInt(currentHealth);
        out.putInt(mana);
        out.putInt(currentMana);
        out.putInt(attack);
        out.putInt(defense);
    }

    @Override
    public void deserialize(ByteBuffer in) throws IOException {
        this.health = in.getInt();
        this.currentHealth = in.getInt();
        this.mana = in.getInt();
        this.currentMana = in.getInt();
        this.attack = in.getInt();
        this.defense = in.getInt();
    }
}
