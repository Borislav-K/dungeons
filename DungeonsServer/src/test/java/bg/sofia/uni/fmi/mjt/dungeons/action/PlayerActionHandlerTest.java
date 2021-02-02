package bg.sofia.uni.fmi.mjt.dungeons.action;

import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
    private Player playerStub;

    private PlayerActionHandler testActionHandler;

    @Before
    public void setUp() throws Exception {
        playerStub = new Player();
        testActionHandler = new PlayerActionHandler(playerManagerMock, gameMapMock);

        when(playerManagerMock.createNewPlayer(playerChannelMock)).thenReturn(playerStub);
        when(playerManagerMock.getPlayerByChannel(playerChannelMock)).thenReturn(playerStub);
        doNothing().when(playerChannelMock).close();
    }

    @Test
    public void testPlayerConnectHandling() throws Exception {
        handleAction(new PlayerConnect(playerChannelMock));
        verify(playerManagerMock).createNewPlayer(playerChannelMock);
        verify(gameMapMock).spawnPlayer(playerStub);
    }

    @Test
    public void testPlayerDisconnectHandling() throws Exception {
        handleAction(new PlayerDisconnect(playerChannelMock));
        verify(playerChannelMock).close();
    }


    private void handleAction(PlayerAction action) {
        testActionHandler.publish(action);
        testActionHandler.handleAll();
    }
}
