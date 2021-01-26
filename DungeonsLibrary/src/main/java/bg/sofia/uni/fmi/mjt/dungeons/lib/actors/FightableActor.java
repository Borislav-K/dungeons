package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class FightableActor implements Actor {

    protected Position2D position;
    protected int health;
    protected int currentHealth;
    protected int mana;
    protected int currentMana;
    protected int attack;
    protected int defense;

    protected FightableActor(int health, int mana, int attack, int defense) {
        setStats(health, mana, attack, defense);
    }

    public FightableActor() {

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
}
