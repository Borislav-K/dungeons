package bg.sofia.uni.fmi.mjt.dungeons.game.state;

import java.io.*;

public class Player implements Externalizable {

    private static final long serialVersionUID=1L;

    private int experience;
    private BattleStats battleStats;

    @Override
    public void writeExternal(ObjectOutput out) {
        throw new UnsupportedOperationException("Player data should only be read from the server");
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.experience = in.readInt();
        this.battleStats = (BattleStats) in.readObject();
    }
}
