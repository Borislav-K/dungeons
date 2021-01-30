package bg.sofia.uni.fmi.mjt.dungeons.lib.network;

import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlayerSegmentTest {

    // Player data
    private static final Weapon dummyWeapon = new Weapon(1, 10);
    private static final Spell dummySpell = new Spell(1, 50, 60);
    private static final List<Item> dummyItems = List.of(
            new HealthPotion(), new ManaPotion(), dummyWeapon, dummySpell);

    private static final Player dummyPlayer = new Player(1);
    private static final Minion dummyMinion = new Minion(3);
    private static final Treasure dummyTreasure = new Treasure();

    // Positions with actors
    private static final Position2D dummyPosition1 = new Position2D(1, 2);
    private static final Position2D dummyPosition2 = new Position2D(3, 4);
    private static final List<Position2D> dummyPositions = List.of(dummyPosition1, dummyPosition2);

    private static final PlayerSegment testSegment = new PlayerSegment(dummyPlayer, dummyPositions);

    @BeforeClass
    public static void init() {
        dummyItems.forEach(dummyPlayer::addItemToInventory);
        dummyPlayer.useItemFromInventory(4); // Learn dummy spell
        dummyPlayer.useItemFromInventory(3); // Equip dummy weapon
        dummyPlayer.addItemToInventory(dummyWeapon);
        dummyPlayer.addItemToInventory(dummySpell);

        dummyPosition1.addActor(dummyPlayer);
        dummyPosition2.addActor(dummyMinion);
        dummyPosition2.addActor(dummyTreasure);
    }

    @Test
    public void testPlayerSegmentTransmission() throws IOException {
        byte[] testSegmentBytes = serializeTestSegment();
        PlayerSegment deserializedTestSegment = deserializeSegment(testSegmentBytes);

        Player deserializedPlayer = deserializedTestSegment.player();
        assertEqualToDummyPlayer(deserializedPlayer);

        List<Position2D> deserializedPositions = deserializedTestSegment.positionsWithActors();
        assertEqualToDummyPositions(deserializedPositions);
    }

    private static byte[] serializeTestSegment() throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        PlayerSegmentTest.testSegment.serialize(dataOutputStream);
        dataOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    private static PlayerSegment deserializeSegment(byte[] bytes) throws IOException {
        var byteArrayInputStream = new ByteArrayInputStream(bytes);
        var dataInputStream = new DataInputStream(byteArrayInputStream);
        PlayerSegment deserializedSegment = new PlayerSegment();
        deserializedSegment.deserialize(dataInputStream);
        return deserializedSegment;
    }

    private static void assertEqualToDummyPlayer(Player player) {
        assertEquals(dummyPlayer, player); // Compares IDs only
        assertEquals(dummyPlayer.position(), player.position());
        assertEquals(dummyPlayer.inventory(), player.inventory());
        assertEquals(dummyPlayer.weapon(), player.weapon());
        assertEquals(dummyPlayer.spell(), player.spell());
    }

    private static void assertEqualToDummyPositions(List<Position2D> positions) {
        assertEquals(2, positions.size());
        assertEquals(dummyPosition1, positions.get(0)); // Compares x,y, isObstaclePosition only
        assertEquals(dummyPosition1.actors(), positions.get(0).actors());
        assertEquals(dummyPosition2, positions.get(1));
        assertEquals(dummyPosition2.actors(), positions.get(1).actors());
    }
}
