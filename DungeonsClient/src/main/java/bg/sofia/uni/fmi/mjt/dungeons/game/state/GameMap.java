package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GameMap implements Externalizable {

    private static final long serialVersionUID = 1L;

    public static final int MAP_DIMENSIONS = 20;
    private byte[][] fields;

    public GameMap() {
        this.fields = new byte[MAP_DIMENSIONS][MAP_DIMENSIONS];
    }

    public byte[][] getFields() {
        return fields;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException("Clients will only receive the game map");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        for (int i = 0; i < MAP_DIMENSIONS; i++) {
            for (int j = 0; j < MAP_DIMENSIONS; j++) {
                fields[i][j] = in.readByte();
            }
        }
    }
}
