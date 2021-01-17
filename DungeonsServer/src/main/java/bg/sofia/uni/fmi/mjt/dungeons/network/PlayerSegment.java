package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.game.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;

import java.io.DataOutputStream;
import java.io.IOException;

// PlayerSegment contains the data relevant only for a single player
public class PlayerSegment {

    private PlayerSegmentType playerSegmentType;
    private GameMap gameMap;
    private Player playerData;

    public PlayerSegment(GameMap gameMap, Player playerData) {
        this.gameMap = gameMap;
        this.playerData = playerData;
        this.playerSegmentType = PlayerSegmentType.INITIAL;
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(playerSegmentType.ordinal());
        gameMap.serialize(out);
        playerData.serialize(out);
    }
}
