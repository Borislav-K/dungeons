package bg.sofia.uni.fmi.mjt.dungeons.game;

import java.io.DataInputStream;
import java.io.IOException;

public class GameMap {

    public static final int MAP_DIMENSIONS = 20;
    private byte[][] fields;

    public GameMap() {
        this.fields = new byte[MAP_DIMENSIONS][MAP_DIMENSIONS];
    }

    public byte[][] getFields() {
        return fields;
    }

    public void deserialize(DataInputStream in) throws IOException {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                fields[i][j] = in.readByte();
            }
        }
    }
}
