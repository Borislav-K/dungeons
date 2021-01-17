package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;

import java.io.DataOutputStream;
import java.io.IOException;

// PlayerSegment contains the data relevant only for a single player
public class PlayerSegment {

    private PlayerSegmentType playerSegmentType;
    private int[] obstacles;
    private Player playerData;

    public PlayerSegment(int[] obstacles, Player playerData) {
        this.obstacles = obstacles;
        this.playerData = playerData;
        this.playerSegmentType = PlayerSegmentType.INITIAL;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(playerSegmentType.ordinal());
        for (int i = 0; i < obstacles.length; i++) {
            out.writeInt(obstacles[i]);
        }
        playerData.serialize(out);
    }
}
