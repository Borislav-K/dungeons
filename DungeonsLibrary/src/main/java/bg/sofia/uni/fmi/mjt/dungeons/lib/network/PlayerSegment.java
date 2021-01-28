package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PlayerSegment implements Transmissible {

    private Player player;
    private List<Position2D> positions;

    public PlayerSegment() {
        player = new Player();
        positions = new LinkedList<>();
    }

    public PlayerSegment(Player player, List<Position2D> positions) {
        this.player = player;
        this.positions = positions;
    }

    public Player player() {
        return player;
    }

    public List<Position2D> positionsWithActors() {
        return positions;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        // Player data
        player.serialize(out);
        player.inventory().serialize(out);

        // Positions with actors
        out.writeByte(positions.size());
        for (Position2D position : positions) {
            position.serialize(out);
        }
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // Player data
        player.deserialize(in);
        player.inventory().deserialize(in);

        // Positions with actors
        int positionsCount = in.readByte();
        for (int i = 1; i <= positionsCount; i++) {
            Position2D position = new Position2D();
            position.deserialize(in);
            positions.add(position);
        }
    }
}
