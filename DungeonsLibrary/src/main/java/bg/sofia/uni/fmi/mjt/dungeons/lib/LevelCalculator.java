package bg.sofia.uni.fmi.mjt.dungeons.lib;

import java.util.Map;

public class LevelCalculator {
    private static final int INITIAL_LEVEL = 1;
    private static final int MAX_LEVEL = 10;

    private static final int RATIO_TO_PERCENTAGE_COEF = 100;

    public static final Map<Integer, Integer> REQUIRED_XP_FOR_LEVEL = Map.of(
            INITIAL_LEVEL, 0,
            2, 100,
            3, 200,
            4, 400,
            5, 650,
            6, 900,
            7, 1200,
            8, 1500,
            9, 2000,
            MAX_LEVEL, 3000
    );

    public static int getLevelByExperience(int experience) {
        for (int i = MAX_LEVEL; i >= INITIAL_LEVEL; i--) {
            if (experience >= REQUIRED_XP_FOR_LEVEL.get(i)) {
                return i;
            }
        }
        throw new RuntimeException(String.format("%s is not a valid experience value", experience));
    }

    public static int getPercentageToNextLevel(int experience) {
        int currentLevel = getLevelByExperience(experience);
        if (currentLevel == MAX_LEVEL) {
            return 0;
        }
        int currentLevelXPLowerBound = REQUIRED_XP_FOR_LEVEL.get(currentLevel);
        int currentLevelXPUpperBound = REQUIRED_XP_FOR_LEVEL.get(currentLevel + 1);
        return (int) Math.round((double) (experience - currentLevelXPLowerBound)
                / (double) (currentLevelXPUpperBound - currentLevelXPLowerBound)
                * RATIO_TO_PERCENTAGE_COEF);
    }

}
