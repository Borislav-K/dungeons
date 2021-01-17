package bg.sofia.uni.fmi.mjt.dungeons.game;

import java.io.DataInputStream;
import java.io.IOException;

public class GameMap {

    public static final int MAP_DIMENSIONS = 20;
    private static final int OBSTACLES_COUNT = 30;
    private int[] obstaclePositions;

    public GameMap() {
        this.obstaclePositions = new int[OBSTACLES_COUNT];
    }

    public int[] getObstacles() {
        return obstaclePositions;
    }

    public void deserialize(DataInputStream in) throws IOException {
        for(int i=0;i<OBSTACLES_COUNT;i++) {
            obstaclePositions[i]=in.readInt();
        }
    }
}
