package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

public class BattleStats implements Externalizable {

    private static final long serialVersionUID = 1;

    public static final BattleStats BASE_PLAYER_STATS = new BattleStats(100, 100, 50, 50);
    private static final int HEALTH_GAIN_PER_LEVEL = 10;
    private static final int MANA_GAIN_PER_LEVEL = 10;
    private static final int ATTACK_GAIN_PER_LEVEL = 50;
    private static final int DEFENSE_GAIN_PER_LEVEL = 50;


    private int health;
    private int mana;
    private int attack;
    private int defense;

    public BattleStats(int health, int mana, int attack, int defense) {
        this.health = health;
        this.mana = mana;
        this.attack = attack;
        this.defense = defense;
    }

    public BattleStats() {
    }

    public void addLevelStats() {
        this.health += HEALTH_GAIN_PER_LEVEL;
        this.mana += MANA_GAIN_PER_LEVEL;
        this.attack += ATTACK_GAIN_PER_LEVEL;
        this.defense += DEFENSE_GAIN_PER_LEVEL;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(health);
        out.writeInt(mana);
        out.writeInt(attack);
        out.writeInt(defense);
    }

    @Override
    public void readExternal(ObjectInput in) {
        throw new UnsupportedOperationException("Battle stats should only be written to clients");
    }

}
