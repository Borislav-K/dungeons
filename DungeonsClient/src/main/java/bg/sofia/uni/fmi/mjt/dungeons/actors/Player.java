package bg.sofia.uni.fmi.mjt.dungeons.actors;

import bg.sofia.uni.fmi.mjt.dungeons.game.BattleStats;

import java.io.*;

public class Player implements Externalizable {

    private static final long serialVersionUID = 1L;

    private int currentLevel;
    private int experiencePercentage;

    private BattleStats battleStats;

    public int currentLevel() {
        return currentLevel;
    }

    public int experiencePercentage() {
        return experiencePercentage;
    }

    public BattleStats battleStats() {
        return battleStats;
    }

    @Override
    public void writeExternal(ObjectOutput out) {
        throw new UnsupportedOperationException("Player data should only be read from the server");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.currentLevel = in.readByte();
        this.experiencePercentage = in.readInt();
        this.battleStats = (BattleStats) in.readObject();
    }
}
