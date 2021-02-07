package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.action.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.atMostOnce;

@RunWith(MockitoJUnitRunner.class)
public class GameServerTest {

    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(10_000);

    @Mock
    private PlayerActionHandler playerActionHandlerMock;
    private GameServer testGameServer;

    @Before
    public void startServer() {
        testGameServer = new GameServer(playerActionHandlerMock);
        testGameServer.start();
    }

    @After
    public void stopServer() throws IOException {
        testGameServer.stop();
    }

    @Test
    public void testPlayerConnection() throws IOException {
        establishConnection();
    }

    @Test
    public void testPlayerDisconnection() throws IOException {
        SocketChannel playerChannel = establishConnection();
        playerChannel.close();
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(PlayerDisconnect.class));
    }

    @Test
    public void testPlayerMovement() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"mvl");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(PlayerMovement.class));
    }

    @Test
    public void testPlayerAttack() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"att");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(PlayerAttack.class));
    }

    @Test
    public void testTreasurePickup() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"pck");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(TreasurePickup.class));
    }

    @Test
    public void testItemUsage() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"us1");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(ItemUsage.class));
    }

    @Test
    public void testItemGrant() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"gv1");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(ItemGrant.class));
    }

    @Test
    public void testItemThrow() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"th1");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(ItemThrow.class));
    }

    @Test
    public void testInvalidCommandsHandling() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"INVALID");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock, atMostOnce()).publish(any());
    }

    @Test
    public void testMultipleCommandsHandling() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"mvlmvr");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock,times(3)).publish(any());
    }

    @Test
    public void testMultipleCommandsWithInvalidAmongstThem() throws IOException {
        SocketChannel playerChannel = establishConnection();
        sendMessageToServer(playerChannel,"mvl---");
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock,times(2)).publish(any());
    }

    private SocketChannel establishConnection() throws IOException {
        SocketChannel playerChannel = SocketChannel.open(SERVER_ADDRESS);
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(PlayerConnect.class));
        return playerChannel;
    }

    private void sendMessageToServer(SocketChannel from, String msg) throws IOException {
        from.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
    }

}
