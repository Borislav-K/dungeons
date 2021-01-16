package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActorType;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

public class Player implements Actor, Externalizable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int experience;
    private Position2D position;
    private BattleStats battleStats;

    public Player(int id, Position2D position, BattleStats battleStats) {
        this.id = id;
        this.position = position;
        this.battleStats = battleStats;
        this.experience = 0;
    }

    public Player() {
    }

    public int id() {
        return id;
    }

    public Position2D position() {
        return this.position;
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(LevelCalculator.getLevelByExperience(experience));
        out.writeInt(LevelCalculator.getPercentageToNextLevel(experience));
        out.writeObject(battleStats);
    }

    @Override
    public void readExternal(ObjectInput in) {
        throw new UnsupportedOperationException("Player data will be distributed by the server only");
    }

    @Override
    public ActorType getType() {
        return ActorType.PLAYER;
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
