package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Player implements Actor {

    private static final int XP_REWARD_PER_PLAYER_LVL = 50;

    private static final int BASE_HEALTH = 150;
    private static final int BASE_MANA = 150;
    private static final int BASE_ATTACK = 40;
    private static final int BASE_DEFENSE = 10;

    private static final int HEALTH_GAIN_PER_LEVEL = 20;
    private static final int MANA_GAIN_PER_LEVEL = 20;
    private static final int ATTACK_GAIN_PER_LEVEL = 15;
    private static final int DEFENSE_GAIN_PER_LEVEL = 10;

    private static BattleStats playerStatsForLevel(int level) {
        return new BattleStats(BASE_HEALTH + HEALTH_GAIN_PER_LEVEL * level,
                BASE_MANA + MANA_GAIN_PER_LEVEL * level,
                BASE_ATTACK + ATTACK_GAIN_PER_LEVEL * level,
                BASE_DEFENSE + DEFENSE_GAIN_PER_LEVEL * level);
    }

    private int id;
    private SocketChannel channel;
    private int experience;
    private Position2D position;
    private BattleStats stats;

    public Player() {
        this.stats = new BattleStats();
    }

    public Player(int id, SocketChannel channel) {
        this.id = id;
        this.channel = channel;
        this.stats = playerStatsForLevel(1);
        this.experience = 0;
    }

    public int id() {
        return id;
    }

    public SocketChannel channel() {
        return channel;
    }

    public int level() {
        return LevelCalculator.getLevelByExperience(experience);
    }

    public int XPPercentage() {
        return LevelCalculator.getPercentageToNextLevel(experience);
    }

    public boolean isDead() {
        return stats.currentHealth() == 0;
    }

    @Override
    public BattleStats stats() {
        return stats;
    }

    @Override
    public Position2D position() {
        return this.position;
    }

    @Override
    public ActorType type() {
        return ActorType.PLAYER;
    }

    @Override
    public int XPReward() {
        return LevelCalculator.getLevelByExperience(experience) * XP_REWARD_PER_PLAYER_LVL;
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }

    public void increaseXP(int amount) {
        int previousLevel = LevelCalculator.getLevelByExperience(experience);
        experience += amount;
        int currentLevel = LevelCalculator.getLevelByExperience(experience);
        if (currentLevel > previousLevel) {
            stats = playerStatsForLevel(currentLevel);
        }
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeInt(position.x());
        out.writeInt(position.y());
        out.writeInt(experience);
        stats.serialize(out);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        id = in.readInt();
        position = new Position2D(in.readInt(), in.readInt());
        experience = in.readInt();
        stats.deserialize(in);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
               "id=" + id +
               ", experience=" + experience +
               ", position=" + position +
               ", battleStats=" + stats +
               '}';
    }
}
