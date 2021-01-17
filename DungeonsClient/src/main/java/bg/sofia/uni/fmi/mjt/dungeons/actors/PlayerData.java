package bg.sofia.uni.fmi.mjt.dungeons.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.BattleStats;
import bg.sofia.uni.fmi.mjt.dungeons.lib.LevelCalculator;

import java.io.DataInputStream;
import java.io.IOException;

public class PlayerData {

    private int id;
    private int posX;
    private int posY;
    private int experience;
    private BattleStats battleStats;

    public PlayerData() {
        this.battleStats = new BattleStats();
    }

    public int currentLevel() {
        return LevelCalculator.getLevelByExperience(experience);
    }

    public int experiencePercentage() {
        return LevelCalculator.getPercentageToNextLevel(experience);
    }

    public int id() {
        return id;
    }

    public int posX() {
        return posX;
    }

    public int posY() {
        return posY;
    }

    public BattleStats battleStats() {
        return battleStats;
    }

    public void deserialize(DataInputStream in) throws IOException {
        id = in.readInt();
        posX = in.readInt();
        posY = in.readInt();
        experience = in.readInt();
        battleStats.deserialize(in);
    }

}
