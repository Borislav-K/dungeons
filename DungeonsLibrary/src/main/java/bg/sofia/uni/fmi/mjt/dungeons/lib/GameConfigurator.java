package bg.sofia.uni.fmi.mjt.dungeons.lib;

import java.util.Arrays;

public class GameConfigurator {
    public static final int MAP_DIMENSIONS = 20;
    public static final int OBSTACLES_COUNT = 40;
    public static final int MINIONS_COUNT = 10;

    private static final int BARRIER_LENGTH = 4 * (MAP_DIMENSIONS - 1);

    public static final int[] OBSTACLE_POSITIONS = initializeObstacles();

    private static int[] initializeObstacles() {
        int[] barrier = buildBarrier();
        int[] obstacles = setObstacles();
        int[] both = Arrays.copyOf(barrier, barrier.length + obstacles.length);
        System.arraycopy(obstacles, 0, both, barrier.length, obstacles.length);
        return both;
    }

    private static int[] buildBarrier() {
        int[] barrierPositions = new int[BARRIER_LENGTH];
        int nextIndex = 0;
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                if (i == 0 || j == 0 || i == MAP_DIMENSIONS - 1 || j == MAP_DIMENSIONS - 1) {
                    barrierPositions[nextIndex++] = i * MAP_DIMENSIONS + j;
                }
            }
        }
        return barrierPositions;
    }

    private static int[] setObstacles() {
        return new int[]{55, 56, 57, 58, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
                93, 94, 95, 96, 97, 221, 222, 223, 224, 225, 226, 227, 228, 248, 268, 288, 308, 328, 348, 354, 355, 356, 357};
    }
}
