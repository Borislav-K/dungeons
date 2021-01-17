package bg.sofia.uni.fmi.mjt.dungeons.actors;

import bg.sofia.uni.fmi.mjt.dungeons.game.BattleStats;

import java.io.*;

public class Player {

    private int currentLevel;
    private int experiencePercentage;
    private BattleStats battleStats;

    public Player() {
        this.battleStats = new BattleStats();
    }

    public int currentLevel() {
        return currentLevel;
    }

    public int experiencePercentage() {
        return experiencePercentage;
    }

    public BattleStats battleStats() {
        return battleStats;
    }

    public void deserialize(DataInputStream in) throws IOException {
        this.currentLevel = in.readInt();
        this.experiencePercentage = in.readInt();
        battleStats.deserialize(in);
    }
}
