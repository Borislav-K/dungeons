package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeadPlayerSegment implements PlayerSegment {

    @Override
    public PlayerSegmentType type() {
        return PlayerSegmentType.DEATH;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(PlayerSegmentType.DEATH.ordinal());
    }

    @Override
    public void deserialize(DataInputStream in) {
    }
}
