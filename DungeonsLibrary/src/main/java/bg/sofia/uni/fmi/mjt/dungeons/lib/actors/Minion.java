package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.SmartBuffer;

import java.io.IOException;
import java.util.Random;

public class Minion extends FightableActor {

    private static final int XP_REWARD_PER_MINION_LVL = 20;

    private static final int BASE_HEALTH = 75;
    private static final int BASE_ATTACK = 25;
    private static final int BASE_DEFENSE = 10;

    private static final int HEALTH_GAIN_PER_LEVEL = 20;
    private static final int ATTACK_GAIN_PER_LEVEL = 15;
    private static final int DEFENSE_GAIN_PER_LEVEL = 10;

    // Minions have no mana as they use no spells
    private void setMinionStatsForLevel(int level) {
        setStats(BASE_HEALTH + HEALTH_GAIN_PER_LEVEL * (level - 1),
                0,
                BASE_ATTACK + ATTACK_GAIN_PER_LEVEL * (level - 1),
                BASE_DEFENSE + DEFENSE_GAIN_PER_LEVEL * (level - 1));
    }

    private static final int MAX_MINION_LEVEL = 5;
    private static final Random generator = new Random();

    private int level;

    public Minion() {
        this.level = generator.nextInt(MAX_MINION_LEVEL) + 1;
        setMinionStatsForLevel(level);
    }

    @Override
    public void dealDamage(FightableActor other) {
        other.takeDamage(attack);
    }

    @Override
    public ActorType type() {
        return ActorType.MINION;
    }

    @Override
    public int XPReward() {
        return level * XP_REWARD_PER_MINION_LVL;
    }

    @Override
    public int level() {
        return level;
    }

    @Override
    public void serialize(SmartBuffer out) throws IOException {
        super.serialize(out);
        out.writeInt(level);
    }

    @Override
    public void deserialize(SmartBuffer in) throws IOException {
        super.deserialize(in);
        level = in.readInt();
    }
}
