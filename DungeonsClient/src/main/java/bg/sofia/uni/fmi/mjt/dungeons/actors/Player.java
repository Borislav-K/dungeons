package bg.sofia.uni.fmi.mjt.dungeons.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;

import java.io.DataInputStream;
import java.io.IOException;

public class Player {

    private int experience;
    private BattleStats battleStats;

    public Player() {
        this.battleStats = new BattleStats();
    }

    public int currentLevel() {
        return LevelCalculator.getLevelByExperience(experience);
    }

    public int experiencePercentage() {
        return LevelCalculator.getPercentageToNextLevel(experience);
    }

    public BattleStats battleStats() {
        return battleStats;
    }

    public void deserialize(DataInputStream in) throws IOException {
        experience = in.readInt();
        battleStats.deserialize(in);
    }
}
