package bg.sofia.uni.fmi.mjt.dungeons.actors;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActorType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;
import bg.sofia.uni.fmi.mjt.dungeons.game.Position2D;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Player implements Actor {

    private static final int XP_REWARD_PER_PLAYER_LVL = 50;

    private int id;
    private SocketChannel channel;
    private int experience;
    private Position2D position;
    private BattleStats battleStats;

    public Player(int id, SocketChannel channel) {
        this.id = id;
        this.channel = channel;
        this.battleStats = BattleStats.getBasePlayerStats();
        this.experience = 0;
    }

    public int id() {
        return id;
    }

    public SocketChannel channel() {
        return channel;
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

        while (currentLevel-- > previousLevel) {
            this.battleStats.levelUp();
        }
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeInt(position.x());
        out.writeInt(position.y());
        out.writeInt(experience);
        battleStats.serialize(out);
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
}
