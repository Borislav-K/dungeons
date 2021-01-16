package bg.sofia.uni.fmi.mjt.dungeons.game;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BattleStats implements Externalizable {

    private static final long serialVersionUID = 1;

    private static final int HEALTH_GAIN_PER_LEVEL = 10;
    private static final int MANA_GAIN_PER_LEVEL = 10;
    private static final int ATTACK_GAIN_PER_LEVEL = 50;
    private static final int DEFENSE_GAIN_PER_LEVEL = 50;


    private int health;
    private int currentHealth;
    private int mana;
    private int currentMana;
    private int attack;
    private int defense;

    public BattleStats(int health, int mana, int attack, int defense) {
        this.health = health;
        this.currentHealth = health;
        this.mana = mana;
        this.currentMana = mana;
        this.attack = attack;
        this.defense = defense;
    }

    public static BattleStats getBasePlayerStats() {
        return new BattleStats(100, 100, 50, 50);
    }

    public BattleStats() {
    }

    public void levelUp() {
        this.health += HEALTH_GAIN_PER_LEVEL;
        this.currentHealth = health;
        this.mana += MANA_GAIN_PER_LEVEL;
        this.currentMana = mana;
        this.attack += ATTACK_GAIN_PER_LEVEL;
        this.defense += DEFENSE_GAIN_PER_LEVEL;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(health);
        out.writeInt(currentHealth);
        out.writeInt(mana);
        out.writeInt(currentMana);
        out.writeInt(attack);
        out.writeInt(defense);
    }

    @Override
    public void readExternal(ObjectInput in) {
        throw new UnsupportedOperationException("Battle stats should only be written to clients");
    }

}
