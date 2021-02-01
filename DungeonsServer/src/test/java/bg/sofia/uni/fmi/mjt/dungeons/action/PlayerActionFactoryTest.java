package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.enums.ActionType;
import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.IllegalPlayerActionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PlayerActionFactoryTest {

    @Mock
    private SocketChannel dummyChannel;

    @Test(expected = IllegalPlayerActionException.class)
    public void testIllegalCommandInputCreation() throws IllegalPlayerActionException {
        PlayerActionFactory.create("ILLEGAL", dummyChannel);
    }

    @Test
    public void testMoveUpCommandCreation() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("mvu", dummyChannel);
        assertMovementAction(createdAction, Direction.UP);
    }

    @Test
    public void testMoveDownCommandCreation() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("mvd", dummyChannel);
        assertMovementAction(createdAction, Direction.DOWN);
    }

    @Test
    public void testMoveLeftCommandCreation() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("mvl", dummyChannel);
        assertMovementAction(createdAction, Direction.LEFT);
    }

    @Test
    public void testMoveRightCommandCreation() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("mvr", dummyChannel);
        assertMovementAction(createdAction, Direction.RIGHT);
    }

    @Test
    public void testAttackCommandCreation() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("att", dummyChannel);
        assertEquals(ActionType.ATTACK, createdAction.type());
    }

    @Test
    public void testTreasurePickupCommandCreation() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("pck", dummyChannel);
        assertEquals(ActionType.TREASURE_PICKUP, createdAction.type());
    }

    @Test(expected = IllegalPlayerActionException.class)
    public void testItemUsageCommandCreationWhenItemNumberIsOutOfBounds() throws IllegalPlayerActionException {
        PlayerActionFactory.create("us10", dummyChannel);
    }

    @Test
    public void testItemUsageCommandCreationOK() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("us1", dummyChannel);
        assertEquals(ActionType.ITEM_USAGE, createdAction.type());
        ItemUsage itemUsage = (ItemUsage) createdAction;
        assertEquals(1, itemUsage.itemNumber());
    }

    @Test(expected = IllegalPlayerActionException.class)
    public void testItemThrowCommandCreationWhenItemIsOutOfBounds() throws IllegalPlayerActionException {
        PlayerActionFactory.create("th10", dummyChannel);
    }

    @Test
    public void testItemThrowCommandCreationOK() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("th1", dummyChannel);
        assertEquals(ActionType.ITEM_THROW, createdAction.type());
        ItemThrow itemThrow = (ItemThrow) createdAction;
        assertEquals(1, itemThrow.itemNumber());
    }

    @Test(expected = IllegalPlayerActionException.class)
    public void testItemGrantCommandCreationWhenItemIsOutOfBounds() throws IllegalPlayerActionException {
        PlayerActionFactory.create("gv10", dummyChannel);
    }

    @Test
    public void testItemGrantCommandCreationOK() throws IllegalPlayerActionException {
        PlayerAction createdAction = PlayerActionFactory.create("gv1", dummyChannel);
        assertEquals(ActionType.ITEM_GRANT, createdAction.type());
        ItemGrant itemGrant = (ItemGrant) createdAction;
        assertEquals(1,itemGrant.itemNumber());
    }

    private static void assertMovementAction(PlayerAction action, Direction direction) {
        assertEquals(ActionType.MOVEMENT, action.type());
        PlayerMovement playerMovement = (PlayerMovement) action;
        assertEquals(direction, playerMovement.direction());
    }

}
