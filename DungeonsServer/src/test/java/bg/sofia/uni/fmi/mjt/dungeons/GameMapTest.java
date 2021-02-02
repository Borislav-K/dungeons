package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.MINIONS_COUNT;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.GameConfigurator.TREASURES_COUNT;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType.MINION;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType.TREASURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameMapTest {

    private GameMap testMap;

    @Before
    public void setUp() {
        testMap = new GameMap();
    }

    @Test
    public void testMinionsAndTreasuresSpawnedCorrectly() {
        List<Position2D> positionsWithActors = testMap.getPositionsWithActors();
        assertEquals(MINIONS_COUNT + TREASURES_COUNT, positionsWithActors.size());
        assertEquals(MINIONS_COUNT, positionsWithMinionsCount());
        assertEquals(TREASURES_COUNT, positionsWithTreasuresCount());
    }

    @Test
    public void testSpawnMinion() {
        testMap.spawnMinion();
        assertEquals(MINIONS_COUNT + 1, positionsWithMinionsCount());
    }

    @Test
    public void testSpawnTreasure() {
        testMap.spawnTreasure();
        assertEquals(TREASURES_COUNT + 1, positionsWithTreasuresCount());
    }

    @Test
    public void testSpawnPlayer() {
        Player player = new Player(1);
        testMap.spawnPlayer(player);
        assertTrue(player.position().actors().contains(player));
        assertEquals(TREASURES_COUNT + MINIONS_COUNT + 1, testMap.getPositionsWithActors().size());
    }

    @Test
    public void testDespawnMinion() {
        Actor randomMinion = randomActor(MINION);
        testMap.despawnActor(randomMinion);
        assertEquals("Minion should respawn immediately", MINIONS_COUNT, positionsWithMinionsCount());
    }

    @Test
    public void testDespawnTreasure() {
        Actor randomTreasure = randomActor(TREASURE);
        testMap.despawnActor(randomTreasure);
        assertEquals("Minion should respawn immediately", TREASURES_COUNT, positionsWithTreasuresCount());
    }

    @Test
    public void testDespawnPlayer() {
        Player player = new Player(1);
        testMap.spawnPlayer(player);
        testMap.despawnActor(player);
        assertEquals(MINIONS_COUNT + TREASURES_COUNT, testMap.getPositionsWithActors().size());
    }

    private long positionsWithMinionsCount() {
        return testMap.getPositionsWithActors().stream()
                .filter(position2D -> position2D.actors().get(0).type().equals(MINION))
                .count();
    }

    private long positionsWithTreasuresCount() {
        return testMap.getPositionsWithActors().stream()
                .filter(position2D -> position2D.actors().get(0).type().equals(TREASURE))
                .count();
    }

    private Actor randomActor(ActorType type) {
        return testMap.getPositionsWithActors().stream()
                .filter(position2D -> position2D.actors().get(0).type().equals(type))
                .map(position2D -> position2D.actors().get(0))
                .findFirst().get();
    }
}
