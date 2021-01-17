package bg.sofia.uni.fmi.mjt.dungeons.game;

import bg.sofia.uni.fmi.mjt.dungeons.game.io.PerformantByteArrayOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;

public class BattleStats {

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

    public void levelUp() {
        this.health += HEALTH_GAIN_PER_LEVEL;
        this.currentHealth = health;
        this.mana += MANA_GAIN_PER_LEVEL;
        this.currentMana = mana;
        this.attack += ATTACK_GAIN_PER_LEVEL;
        this.defense += DEFENSE_GAIN_PER_LEVEL;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(health);
        out.writeInt(currentHealth);
        out.writeInt(mana);
        out.writeInt(currentMana);
        out.writeInt(attack);
        out.writeInt(defense);
    }
}
