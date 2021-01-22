package bg.sofia.uni.fmi.mjt.dungeons.lib;

import java.util.Map;

public class LevelCalculator {
    public static final int INITIAL_LEVEL = 1;
    private static final int MAX_LEVEL = 10;

    private static final int RATIO_TO_PERCENTAGE_COEF = 100;

    private static final Map<Integer, Double> REQUIRED_XP_FOR_LEVEL = Map.of(
            INITIAL_LEVEL, 0.0,
            2, 100.0,
            3, 200.0,
            4, 400.0,
            5, 650.0,
            6, 900.0,
            7, 1200.0,
            8, 1500.0,
            9, 2000.0,
            MAX_LEVEL, 3000.0
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
        double currentLevelXPLowerBound = REQUIRED_XP_FOR_LEVEL.get(currentLevel);
        double currentLevelXPUpperBound = REQUIRED_XP_FOR_LEVEL.get(currentLevel + 1);
        return (int) Math.round((experience - currentLevelXPLowerBound)
                                / (currentLevelXPUpperBound - currentLevelXPLowerBound)
                                * RATIO_TO_PERCENTAGE_COEF);
    }

}
