package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActorType;

import java.util.Map;
import java.util.Random;

public class Minion implements Actor {

    private static final int XP_REWARD_PER_MINION_LVL = 100;

    private static final Map<Integer, BattleStats> MINION_STATS = Map.of(
            1, new BattleStats(50, 0, 25, 20),
            2, new BattleStats(65, 0, 35, 25),
            3, new BattleStats(80, 0, 45, 30),
            4, new BattleStats(100, 0, 60, 35),
            5, new BattleStats(120, 0, 80, 40));

    private static final Random generator = new Random();
    private static final int MAX_MINION_LEVEL = 5;

    private int level;
    private BattleStats stats;

    public Minion() {
        this.level = generator.nextInt() % (MAX_MINION_LEVEL + 1);
        this.stats = MINION_STATS.get(level);
    }

    @Override
    public ActorType getType() {
        return ActorType.MINION;
    }

    public int getXPReward() {
        return this.level * XP_REWARD_PER_MINION_LVL;
    }
}
