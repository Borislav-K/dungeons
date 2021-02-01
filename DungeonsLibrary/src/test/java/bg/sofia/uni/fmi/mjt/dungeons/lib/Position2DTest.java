package bg.sofia.uni.fmi.mjt.dungeons.lib;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Actor;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Treasure;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType.MINION;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType.PLAYER;
import static bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ActorType.TREASURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class Position2DTest {

    private static final int MAX_ACTORS_AT_POSITION = 2;

    private static final Actor dummyPlayer = new Player(1);
    private static final Actor dummyMinion = new Minion();
    private static final Actor dummyTreasure = new Treasure();


    private Position2D testPosition;

    @Before
    public void setUp() {
        testPosition = new Position2D(1, 2);
    }

    @Test
    public void testPositionActorLimit() {
        testPosition.addActor(dummyPlayer);
        testPosition.addActor(dummyMinion);
        testPosition.addActor(dummyTreasure);

        List<Actor> actorsAtPosition = testPosition.actors();
        assertEquals(MAX_ACTORS_AT_POSITION, actorsAtPosition.size());
        assertTrue(actorsAtPosition.contains(dummyPlayer));
        assertTrue(actorsAtPosition.contains(dummyMinion));

        assertFalse(testPosition.containsFreeSpace());
    }

    @Test
    public void testIsSpawnable() {
        assertTrue(testPosition.isSpawnable());
        testPosition.addActor(dummyPlayer);
        assertFalse(testPosition.isSpawnable());
    }

    @Test
    public void testContainsFreeSpace() {
        assertTrue(testPosition.containsFreeSpace());
        testPosition.addActor(dummyPlayer);
        assertTrue(testPosition.containsFreeSpace());
        testPosition.addActor(dummyMinion);
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
        assertNull(testPosition.getActorNotSameAs(dummyPlayer, PLAYER, MINION, TREASURE));
    }

    @Test
    public void testGetActorNotSameAsWhenGivenActorIsPresentAndGivenTypeIsNot() {
        testPosition.addActor(dummyPlayer);
        testPosition.addActor(dummyMinion);
        assertNull(testPosition.getActorNotSameAs(dummyPlayer, PLAYER, TREASURE));
    }

    @Test
    public void testGetActorNotSameAsWhenGivenActorIsPresentAndGivenTypeIs() {
        testPosition.addActor(dummyPlayer);
        testPosition.addActor(dummyMinion);
        assertEquals(dummyMinion, testPosition.getActorNotSameAs(dummyPlayer, MINION));
    }

}
