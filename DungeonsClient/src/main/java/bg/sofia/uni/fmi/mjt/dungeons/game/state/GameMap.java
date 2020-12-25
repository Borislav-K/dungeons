package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.Serializable;

public class GameMap implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MAP_DIMENSIONS = 20;
    private char[][] fields;

    public GameMap() {
        this.fields = new char[MAP_DIMENSIONS][MAP_DIMENSIONS];
    }

    public char[][] getFields() {
        return fields;
    }
}
