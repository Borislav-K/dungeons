package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeadPlayerSegment implements PlayerSegment {

    private Player player;

    public DeadPlayerSegment() {
        this.player = new Player();
    }

    public DeadPlayerSegment(Player player) {
        this.player = player;
    }

    @Override
    public PlayerSegmentType type() {
        return PlayerSegmentType.DEATH;
    }

    @Override
    public Player player() {
        return player;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(PlayerSegmentType.DEATH.ordinal());
        player.serialize(out);
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        player.deserialize(in);
    }
}
