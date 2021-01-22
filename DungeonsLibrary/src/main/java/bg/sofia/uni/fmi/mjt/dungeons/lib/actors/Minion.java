package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class Minion implements Actor {

    private static final int XP_REWARD_PER_MINION_LVL = 20;

    private static final int BASE_HEALTH = 50;
    private static final int BASE_ATTACK = 15;
    private static final int BASE_DEFENSE = 10;

    private static final int HEALTH_GAIN_PER_LEVEL = 20;
    private static final int ATTACK_GAIN_PER_LEVEL = 15;
    private static final int DEFENSE_GAIN_PER_LEVEL = 10;

    // Minions have no mana as they use no spells
    private static BattleStats minionStatsForLevel(int level) {
        return new BattleStats(BASE_HEALTH + HEALTH_GAIN_PER_LEVEL * level, 0,
                BASE_ATTACK + ATTACK_GAIN_PER_LEVEL * level,
                BASE_DEFENSE + DEFENSE_GAIN_PER_LEVEL * level);
    }

    private static final Random generator = new Random();
    private static final int MAX_MINION_LEVEL = 5;

    private int level;
    private BattleStats stats;
    private Position2D position;

    public Minion() {
        this.level = generator.nextInt(MAX_MINION_LEVEL) + 1;
        System.out.printf("Spawning minion with level %d\n", level);
        this.stats = minionStatsForLevel(level);
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }

    @Override
    public BattleStats stats() {
        return stats;
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
    public Position2D position() {
        return position;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(level);
        out.writeInt(position.x());
        out.writeInt(position.y());
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        level = in.readInt();
        position = new Position2D(in.readInt(), in.readInt());
        stats = minionStatsForLevel(level);
    }

    @Override
    public String toString() {
        return "Minion{" +
               "level=" + level +
               ", stats=" + stats +
               ", position=" + position +
               '}';
    }
}