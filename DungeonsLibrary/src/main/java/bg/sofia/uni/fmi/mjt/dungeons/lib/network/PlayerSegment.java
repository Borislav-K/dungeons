package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// PlayerSegment contains the data relevant only for a single player
public class PlayerSegment implements Transmissible {

    private PlayerSegmentType playerSegmentType;
    private Player player;
    private List<Position2D> positions;

    public PlayerSegment() {
        player = new Player();
        positions = new LinkedList<>();
    }

    public PlayerSegment(Player player, List<Position2D> positions) {
        this.player = player;
        this.positions = positions;
        this.playerSegmentType = PlayerSegmentType.INITIAL;
    }

    public PlayerSegmentType type() {
        return playerSegmentType;
    }

    public Player player() {
        return player;
    }

    public List<Position2D> positionsWithActors() {
        return positions;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        out.writeInt(playerSegmentType.ordinal());
        player.serialize(out);
        out.writeInt(positions.size());
        for (Position2D position : positions) {
            position.serialize(out);
        }
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        playerSegmentType = PlayerSegmentType.values()[in.readInt()];
        player.deserialize(in);
        int positionsCount = in.readInt();
        for (int i = 1; i <= positionsCount; i++) {
            Position2D position = new Position2D();
            position.deserialize(in);
            positions.add(position);
        }
    }
}
