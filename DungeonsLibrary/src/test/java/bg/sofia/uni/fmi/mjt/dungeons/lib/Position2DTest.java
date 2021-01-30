package bg.sofia.uni.fmi.mjt.dungeons.lib;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Treasure;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType.*;
import static org.junit.Assert.*;

public class Position2DTest {

    private static final int MAX_ACTORS_AT_POSITION = 2;

    private static final Actor testPlayer = new Player(1);
    private static final Actor testMinion = new Minion();
    private static final Actor testTreasure = new Treasure();


    private static Position2D testPosition;

    @Before
    public void setUp() {
        testPosition = new Position2D(1, 2);
    }

    @Test
    public void testPositionActorLimit() {
        testPosition.addActor(testPlayer);
        testPosition.addActor(testMinion);
        testPosition.addActor(testTreasure);

        List<Actor> actorsAtPosition = testPosition.actors();
        assertEquals(MAX_ACTORS_AT_POSITION, actorsAtPosition.size());
        assertTrue(actorsAtPosition.contains(testPlayer));
        assertTrue(actorsAtPosition.contains(testMinion));

        assertFalse(testPosition.containsFreeSpace());
    }

    @Test
    public void testIsSpawnable() {
        assertTrue(testPosition.isSpawnable());
        testPosition.addActor(testPlayer);
        assertFalse(testPosition.isSpawnable());
    }

    @Test
    public void testContainsFreeSpace() {
        assertTrue(testPosition.containsFreeSpace());
        testPosition.addActor(testPlayer);
        assertTrue(testPosition.containsFreeSpace());
        testPosition.addActor(testMinion);
        assertFalse(testPosition.containsFreeSpace());
    }

    @Test
    public void testMarkAsObstacle() {
        testPosition.markAsObstacle();
        assertFalse(testPosition.isSpawnable());
        assertFalse(testPosition.containsFreeSpace());
    }

    @Test
    public void testGetActorNotSameAsWhenGivenActorIsNotPresent() {
        assertNull(testPosition.getActorNotSameAs(testPlayer, PLAYER, MINION, TREASURE));
    }

    @Test
    public void testGetActorNotSameAsWhenGivenActorIsPresentAndGivenTypeIsNot() {
        testPosition.addActor(testPlayer);
        testPosition.addActor(testMinion);
        assertNull(testPosition.getActorNotSameAs(testPlayer, PLAYER, TREASURE));
    }

    @Test
    public void testGetActorNotSameAsWhenGivenActorIsPresentAndGivenTypeIs() {
        testPosition.addActor(testPlayer);
        testPosition.addActor(testMinion);
        assertEquals(testMinion, testPosition.getActorNotSameAs(testPlayer, MINION));
    }

}
