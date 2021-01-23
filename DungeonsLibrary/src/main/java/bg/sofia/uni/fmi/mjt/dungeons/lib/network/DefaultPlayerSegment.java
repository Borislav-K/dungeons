package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.PlayerSegmentType;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.position.Position2D;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DefaultPlayerSegment implements PlayerSegment {

    private Player player;
    private List<Position2D> positions;

    public DefaultPlayerSegment() {
        player = new Player();
        positions = new LinkedList<>();
    }

    public DefaultPlayerSegment(Player player, List<Position2D> positions) {
        this.player = player;
        this.positions = positions;
    }

    public PlayerSegmentType type() {
        return PlayerSegmentType.DEFAULT;
    }

    public Player player() {
        return player;
    }

    public List<Position2D> positionsWithActors() {
        return positions;
    }

    @Override
    public void serialize(DataOutputStream out) throws IOException {
        // Segment Type
        out.writeInt(PlayerSegmentType.DEFAULT.ordinal());

        // Player data
        player.serialize(out);
        player.inventory().serialize(out);

        // Positions with actors
        out.writeInt(positions.size());
        for (Position2D position : positions) {
            position.serialize(out);
        }
    }

    @Override
    public void deserialize(DataInputStream in) throws IOException {
        // Player data
        player.deserialize(in);
        player.inventory().deserialize(in);
        int positionsCount = in.readInt();
        for (int i = 1; i <= positionsCount; i++) {
            Position2D position = new Position2D();
            position.deserialize(in);
            positions.add(position);
        }
    }
}
