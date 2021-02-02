package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.enums.Direction;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Treasure;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerActionHandlerTest {
    @Mock
    private PlayerManager playerManagerMock;
    @Mock
    private GameMap gameMapMock;
    @Mock
    private SocketChannel playerChannelMock;
    @Mock
    private Position2D positionMock;
    @Spy
    private Player playerSpy;

    private PlayerActionHandler testActionHandler;

    @Before
    public void setUp() throws Exception {
        playerSpy = spy(new Player(1));
        playerSpy.setPosition(positionMock);

        testActionHandler = new PlayerActionHandler(playerManagerMock, gameMapMock);
        when(playerManagerMock.createNewPlayer(playerChannelMock)).thenReturn(playerSpy);
        when(playerManagerMock.getPlayerByChannel(playerChannelMock)).thenReturn(playerSpy);
        doNothing().when(playerChannelMock).close();
    }

    @Test
    public void testPlayerConnectHandling() throws Exception {
        handleAction(new PlayerConnect(playerChannelMock));
        verify(playerManagerMock).createNewPlayer(playerChannelMock);
        verify(gameMapMock).spawnPlayer(playerSpy);
    }

    @Test
    public void testPlayerDisconnectHandling() throws Exception {
        handleAction(new PlayerDisconnect(playerChannelMock));
        verify(playerManagerMock).removePlayer(playerSpy);
        verify(gameMapMock).despawnActor(playerSpy);
        verify(playerChannelMock).close();
    }

    @Test
    public void testPlayerMovementHandling() {
        handleAction(new PlayerMovement(playerChannelMock, Direction.UP));
        verify(gameMapMock).movePlayer(playerSpy, Direction.UP);
    }

    @Test
    public void testPlayerAttackHandlingWhenPlayerWins() {
        Minion opponent = new Minion(1);
        when(positionMock.getActorNotSameAs(any(), any())).thenReturn(opponent);

        handleAction(new PlayerAttack(playerChannelMock));
        verify(gameMapMock).despawnActor(opponent);
        verify(playerSpy).increaseXP(opponent.XPReward());
    }

    @Test
    public void testPlayerAttackHandlingWhenPlayerLoses() {
        Minion opponent = new Minion(5);
        when(positionMock.getActorNotSameAs(any(), any())).thenReturn(opponent);

        handleAction(new PlayerAttack(playerChannelMock));
        verify(gameMapMock).despawnActor(playerSpy);
        verify(playerSpy).sufferDeathPenalty();
        verify(gameMapMock).spawnPlayer(playerSpy);
    }

    @Test
    public void testPlayerAttackHandlingWhenPlayerIsAloneOnPosition() {
        when(positionMock.getActorNotSameAs(any(), any())).thenReturn(null);

        handleAction(new PlayerAttack(playerChannelMock));
        verify(gameMapMock, never()).despawnActor(any());
    }

    @Test
    public void testTreasurePickupHandlingWhenThereIsNoTreasure() {
        when(positionMock.getActorNotSameAs(any(),any())).thenReturn(null);

        handleAction(new TreasurePickup(playerChannelMock));
        verify(gameMapMock, never()).despawnActor(any());
        verify(playerSpy, never()).addItemToInventory(any());
    }

    @Test
    public void testTreasurePickupHandlingWhenThereIsTreasure() {
        Treasure treasure = new Treasure();
        when(positionMock.getActorNotSameAs(any(),any())).thenReturn(treasure);

        handleAction(new TreasurePickup(playerChannelMock));
        verify(gameMapMock).despawnActor(treasure);
        verify(playerSpy).addItemToInventory(any(Item.class));
    }

    @Test
    public void testItemUsageHandling() {
        handleAction(new ItemUsage(playerChannelMock,3));
        verify(playerSpy).useItemFromInventory(3);
    }

    @Test
    public void testItemThrowHandling() {
        handleAction(new ItemThrow(playerChannelMock,2));
        verify(playerSpy).removeItemFromInventory(2);
    }

    @Test
    public void testItemGrantHandlingWhenThereIsNoOtherPlayer() {
        when(positionMock.getActorNotSameAs(any(),any())).thenReturn(null);
        handleAction(new ItemGrant(playerChannelMock,1));
        verify(playerSpy, never()).removeItemFromInventory(1);
    }

    @Test
    public void testItemGrantHandlingWhenThereIsPlayerButGiverHasNoItemAtThisIndex() {
        Player receiver = spy(new Player(2));
        when(positionMock.getActorNotSameAs(any(),any())).thenReturn(receiver);
        handleAction(new ItemGrant(playerChannelMock,1));
        verify(playerSpy).removeItemFromInventory(1);
        verify(receiver,never()).addItemToInventory(any());
    }

    @Test
    public void testItemGrantHandlingOK() {
        Player receiver = spy(new Player(2));
        when(positionMock.getActorNotSameAs(any(),any())).thenReturn(receiver);
        Item itemToGrant = new HealthPotion();
        playerSpy.addItemToInventory(itemToGrant);
        handleAction(new ItemGrant(playerChannelMock,1));
        verify(playerSpy).removeItemFromInventory(1);
        verify(receiver).addItemToInventory(itemToGrant);
    }

    @Test
    public void testPlayerActionHandlingWhenPlayerIsRemovedFromPlayerManager() throws Exception {
        when(playerManagerMock.getPlayerByChannel(any())).thenThrow(NoSuchPlayerException.class);
        handleAction(new PlayerMovement(playerChannelMock,Direction.UP));
        verify(playerChannelMock).close();
    }

    private void handleAction(PlayerAction action) {
        testActionHandler.publish(action);
        testActionHandler.handleAll();
    }
}
